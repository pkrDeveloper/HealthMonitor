package com.healthmonitor.app.business.facecamera

import android.util.Log
import com.google.mlkit.vision.face.Face

object FaceProcessor {
    fun analyzeFace(face: Face): String {
        val leftEyeOpenProb = face.leftEyeOpenProbability ?: 0.0f
        val rightEyeOpenProb = face.rightEyeOpenProbability ?: 0.0f
        val smilingProb = face.smilingProbability ?: 0.0f
        val headTiltX = face.headEulerAngleX
        val headTiltY = face.headEulerAngleY

        val report = StringBuilder().apply {
            append("Health Report:\n")
            append("• Left Eye Open: ${"%.2f".format(leftEyeOpenProb)}\n")
            append("• Right Eye Open: ${"%.2f".format(rightEyeOpenProb)}\n")
            append("• Smiling Probability: ${"%.2f".format(smilingProb)}\n")
            append("• Head Tilt X: ${"%.2f".format(headTiltX)}°\n")
            append("• Head Tilt Y: ${"%.2f".format(headTiltY)}°\n\n")

            if (smilingProb < 0.2f && leftEyeOpenProb < 0.5f && rightEyeOpenProb < 0.5f) {
                append("⚠️ You appear drowsy or tired. Consider taking a break.\n")
            }
            if (headTiltX > 15 || headTiltY > 15) {
                append("⚠️ Your head is tilted. Try maintaining a straight posture.\n")
            }
            if (smilingProb > 0.5f) {
                append("😊 You are smiling! Keep up the positivity!\n")
            }
        }

        Log.d("HealthMonitor", report.toString())
        return report.toString()
    }
}