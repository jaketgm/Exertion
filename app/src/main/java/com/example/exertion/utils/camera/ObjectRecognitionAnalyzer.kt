package com.example.exertion.utils.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.exertion.data.ec.ECFrameMetrics
import com.example.exertion.data.ec.ECPhase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicLong
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.exp

fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(filename)
    val inputStream = fileDescriptor.createInputStream()
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun loadLabels(context: Context, filename: String): List<String> {
    return context.assets.open(filename)
        .bufferedReader()
        .useLines { lines ->
            lines.filter { it.isNotBlank() }.map { it.trim() }.toList()
        }
}

class ObjectRecognitionAnalyzer(
    private val context: Context,
    private val onDetectedObjectUpdated: (String) -> Unit,
    private val onFrameMetrics: ((ECFrameMetrics) -> Unit)? = null
) : ImageAnalysis.Analyzer {
    companion object {
        const val THROTTLE_TIMEOUT_MS = 1_000L
        private const val MODEL_INPUT_SIZE = 640
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    @Volatile
    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()
    private val last_analyzed_time = AtomicLong(0L)

    private var lastYCenter: Float? = null
    private var lastYTime: Long? = null
    private var minY: Float = Float.POSITIVE_INFINITY
    private var maxY: Float = Float.NEGATIVE_INFINITY

    init {
        scope.launch {
            try {
                Log.d("Analyzer", "Loading model file...")
                val modelBuffer = loadModelFile(context, "best_float32.tflite")
                Log.d("Analyzer", "Model loaded: ${modelBuffer.capacity()} bytes")

                labels = loadLabels(context, "labels.txt")
                Log.d("Analyzer", "Loaded ${labels.size} labels.")

                val options = Interpreter.Options().apply { setNumThreads(4) }
                val loadedInterpreter = Interpreter(modelBuffer, options)
                interpreter = loadedInterpreter
                Log.d("Analyzer", "Interpreter initialized successfully.")

                val shape = loadedInterpreter.getOutputTensor(0).shape()
                Log.d("Analyzer", "Output tensor shape: ${shape.contentToString()}")
                Log.d("Analyzer", "Output tensor type: ${loadedInterpreter.getOutputTensor(0).dataType()}")
            } catch (e: Exception) {
                Log.e("Analyzer", "Error initializing TFLite interpreter", e)
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        try {
            val current_time = System.currentTimeMillis()
            if (current_time - last_analyzed_time.get() < THROTTLE_TIMEOUT_MS) {
                image.close()
                return
            }
            last_analyzed_time.set(current_time)
            Log.d("Analyzer", "Analyzing frame ${image.width}x${image.height}")
            val bitmap = imageProxyToBitMap(image)
            image.close()
            scope.launch {
                val result = detectObjects(bitmap)
                withContext(Dispatchers.Main) {
                    onDetectedObjectUpdated(result)
                }
            }
        } catch (e: Exception) {
            Log.e("Analyzer", "Error during analyze()", e)
            image.close()
        }
    }

    private fun imageProxyToBitMap(image: ImageProxy): Bitmap {
        val y_buffer = image.planes[0].buffer
        val u_buffer = image.planes[1].buffer
        val v_buffer = image.planes[2].buffer

        val ySize = y_buffer.remaining()
        val uSize = u_buffer.remaining()
        val vSize = v_buffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        y_buffer.get(nv21, 0, ySize)
        v_buffer.get(nv21, ySize, vSize)
        u_buffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 90, out)
        val jpegBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
    }

    private fun sigmoid(x: Float): Float {
        return (1f / (1f + exp(-x)))
    }

    fun detectObjects(bitmap: Bitmap): String {
        Log.d("Analyzer", "Starting inference on bitmap ${bitmap.width}x${bitmap.height}")

        // Resize to model input size
        val inputBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, true)
        val inputBuffer = convertBitmapToByteBuffer(inputBitmap)

        val output = Array(1) { Array(17) { FloatArray(8400) } }

        try {
            val startTime = System.currentTimeMillis()
            interpreter?.run(inputBuffer, output)
            val endTime = System.currentTimeMillis()
            Log.d("Analyzer", "Model inference finished in ${endTime - startTime} ms")

            for (i in 0 until 5) {
                Log.d(
                    "Analyzer",
                    "Anchor $i conf=${output[0][4][i]} firstClass=${output[0][5][i]}"
                )
            }

        } catch (e: Exception) {
            Log.e("Analyzer", "Error running model inference", e)
            return "Model inference failed"
        }

        var bestConf = 0f
        var bestClass = -1

        val numAnchors = output[0][0].size
        val numChannels = output[0].size
        val numClasses = numChannels - 5
        Log.d("Analyzer", "Detected $numClasses classes, $numAnchors anchors per frame.")

        val maxRawConf = output[0][4].maxOrNull() ?: 0f
        val meanRawConf = output[0][4].average()
        Log.d("Analyzer", "Raw confidence stats -> max=$maxRawConf, mean=$meanRawConf")

        for (i in 0 until numAnchors) {
            val conf = sigmoid(output[0][4][i])
            if (conf > 0.05f) {
                var maxProb = 0f
                var clsIdx = -1
                for (c in 5 until numChannels) {
                    val prob = sigmoid(output[0][c][i])
                    if (prob > maxProb) {
                        maxProb = prob
                        clsIdx = c - 5
                    }
                }
                val totalConf = conf * maxProb
                if (totalConf > bestConf) {
                    bestConf = totalConf
                    bestClass = clsIdx
                }
            }
        }

        if (onFrameMetrics != null) {

            val now = System.currentTimeMillis()

            // For the best anchor: find anchor index again
            var yCenterRaw: Float? = null

            if (bestClass >= 0) {
                var bestIdx = -1
                var bestScore = 0f

                for (i in 0 until numAnchors) {
                    val conf = sigmoid(output[0][4][i])
                    if (conf > 0.05f) {

                        var maxProb = 0f
                        for (c in 5 until numChannels) {
                            val prob = sigmoid(output[0][c][i])
                            if (prob > maxProb) maxProb = prob
                        }

                        val total = conf * maxProb
                        if (total > bestScore) {
                            bestScore = total
                            bestIdx = i
                        }
                    }
                }

                if (bestIdx >= 0) {
                    yCenterRaw = output[0][1][bestIdx]
                }
            }

            if (yCenterRaw != null) {
                val y = yCenterRaw

                if (y < minY) minY = y
                if (y > maxY) maxY = y

                val span = (maxY - minY).takeIf { it > 1e-4f } ?: 1f
                val romFraction = ((y - minY) / span).coerceIn(0f, 1f)

                val lastY = lastYCenter
                val lastT = lastYTime

                var velocity = 0f
                if (lastY != null && lastT != null) {
                    val dt = (now - lastT).coerceAtLeast(1).toFloat() / 1000f
                    velocity = (romFraction - lastY) / dt
                }

                lastYCenter = romFraction
                lastYTime = now

                val phase = when {
                    velocity > 0.05f -> ECPhase.CONCENTRIC
                    velocity < -0.05f -> ECPhase.ECCENTRIC
                    else -> ECPhase.IDLE
                }

                val metrics = ECFrameMetrics(
                    timestampMs = now,
                    romFraction = romFraction,
                    velocity = velocity,
                    phase = phase
                )

                onFrameMetrics.invoke(metrics)
            }
        }

        return if (bestClass >= 0)
            "${labels.getOrElse(bestClass) { "Unknown" }} (${String.format("%.1f", bestConf * 100)}%)"
        else
            "No object detected"
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * MODEL_INPUT_SIZE * MODEL_INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(MODEL_INPUT_SIZE * MODEL_INPUT_SIZE)
        bitmap.getPixels(intValues, 0, MODEL_INPUT_SIZE, 0, 0, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE)

        for (pixel in intValues) {
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            byteBuffer.putFloat(r * 2 - 1)
            byteBuffer.putFloat(g * 2 - 1)
            byteBuffer.putFloat(b * 2 - 1)
        }

        return byteBuffer
    }
}