package com.applemapsredirect

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var inputUrl: TextInputEditText
    private lateinit var btnConvert: MaterialButton
    private lateinit var resultCard: MaterialCardView
    private lateinit var resultText: TextView
    private lateinit var btnCopy: MaterialButton
    private lateinit var btnOpen: MaterialButton

    private var currentGoogleUri: String? = null
    private var currentWebUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputUrl = findViewById(R.id.input_url)
        btnConvert = findViewById(R.id.btn_convert)
        resultCard = findViewById(R.id.result_card)
        resultText = findViewById(R.id.result_text)
        btnCopy = findViewById(R.id.btn_copy)
        btnOpen = findViewById(R.id.btn_open)

        btnConvert.setOnClickListener { convert() }

        btnCopy.setOnClickListener {
            currentWebUrl?.let { url ->
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Google Maps Link", url))
                btnCopy.text = "Copied!"
                btnCopy.postDelayed({ btnCopy.text = "Copy to Clipboard" }, 1500)
            }
        }

        btnOpen.setOnClickListener {
            currentGoogleUri?.let { uri ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                val chooser = Intent.createChooser(intent, "Open with")
                startActivity(chooser)
            }
        }
    }

    private fun convert() {
        val url = inputUrl.text?.toString()?.trim()
        if (url.isNullOrBlank()) {
            showError("Please paste an Apple Maps link")
            return
        }

        if (!url.contains("maps.apple.com")) {
            showError("This doesn't look like an Apple Maps link")
            return
        }

        btnConvert.isEnabled = false
        btnConvert.text = "Converting..."

        lifecycleScope.launch {
            try {
                val parsed = AppleMapsParser.parse(url)
                currentGoogleUri = parsed.toGoogleMapsUri()
                currentWebUrl = parsed.toGoogleMapsWebUrl()

                resultText.text = currentWebUrl
                resultText.setTextColor(getColor(R.color.text_primary))
                resultCard.visibility = View.VISIBLE
                btnCopy.visibility = View.VISIBLE
                btnOpen.visibility = View.VISIBLE
            } catch (e: Exception) {
                showError(e.message ?: "Failed to parse the Apple Maps link")
            } finally {
                btnConvert.isEnabled = true
                btnConvert.text = "Convert"
            }
        }
    }

    private fun showError(message: String) {
        resultCard.visibility = View.VISIBLE
        resultText.text = message
        resultText.setTextColor(getColor(R.color.error_red))
        btnCopy.visibility = View.GONE
        btnOpen.visibility = View.GONE
        currentGoogleUri = null
        currentWebUrl = null
    }
}
