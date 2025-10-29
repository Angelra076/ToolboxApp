package com.angel.toolboxapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val linkedinBtn = findViewById<Button>(R.id.linkedinBtn)
        linkedinBtn.setOnClickListener {
            val url = "https://www.linkedin.com/in/angel-ramirez-599b951b8/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}