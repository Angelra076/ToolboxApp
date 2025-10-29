package com.angel.toolboxapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import kotlin.concurrent.thread

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val input = findViewById<EditText>(R.id.cityInput)
        val button = findViewById<Button>(R.id.checkWeatherBtn)
        val result = findViewById<TextView>(R.id.weatherResult)

        button.setOnClickListener {
            val city = input.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Escribe una ciudad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            result.text = "Consultando clima..."
            val encodedCity = URLEncoder.encode(city, "UTF-8")

            thread {
                try {
                    val url = URL("https://wttr.in/$encodedCity?format=j1")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    val response = connection.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)

                    val current = json.getJSONArray("current_condition").getJSONObject(0)
                    val temp = current.getString("temp_C")
                    val feelsLike = current.getString("FeelsLikeC")
                    val desc = current.getJSONArray("weatherDesc").getJSONObject(0).getString("value")

                    runOnUiThread {
                        result.text = "🌡️ Temp: $temp°C\n🤗 Sensación: $feelsLike°C\n☁️ Estado: $desc"
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        result.text = "Error: ${e.message}"
                    }
                }
            }
        }
    }
}