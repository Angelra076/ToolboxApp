package com.angel.toolboxapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGender).setOnClickListener {
            startActivity(Intent(this, GenderActivity::class.java))
        }

        findViewById<Button>(R.id.btnAge).setOnClickListener {
            startActivity(Intent(this, AgeActivity::class.java))
        }

        findViewById<Button>(R.id.btnUniversity).setOnClickListener {
            startActivity(Intent(this, UniversityActivity::class.java))
        }

        findViewById<Button>(R.id.btnWeather).setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }

        findViewById<Button>(R.id.btnPokemon).setOnClickListener {
            startActivity(Intent(this, PokemonActivity::class.java))
        }

        findViewById<Button>(R.id.btnWordpress).setOnClickListener {
            startActivity(Intent(this, WordpressActivity::class.java))
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}