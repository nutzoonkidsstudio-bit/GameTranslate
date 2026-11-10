package com.example.altyazi

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class CaptionEngine(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "BURAYA_API_ANAHTARINI_YAZ" // Test aşamasında buraya kendi API key'ini ekleyeceğiz
    )
    
    // Cihazı yormamak için saniyede 2 kare (500ms) limiti
    private var lastProcessTime = 0L

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("tr", "TR")
        }
    }

    fun processImage(image: InputImage) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastProcessTime < 500) return 
        lastProcessTime = currentTime

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedText = visionText.text
                if (detectedText.isNotBlank()) {
                    translateAndSpeak(detectedText)
                }
            }
    }

    private fun translateAndSpeak(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prompt = "Şu oyun altyazısını doğal ve kısa bir şekilde Türkçeye çevir. Sadece çeviriyi ver: $text"
                val response = generativeModel.generateContent(prompt)
                val translatedText = response.text?.trim() ?: ""
                
                if (translatedText.isNotBlank()) {
                    tts?.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } catch (e: Exception) {
                Log.e("Gemini", "Çeviri hatası", e)
            }
        }
    }
}