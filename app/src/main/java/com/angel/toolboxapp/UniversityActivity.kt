package com.angel.toolboxapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class UniversityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university)

        val title = findViewById<TextView>(R.id.titleText)
        val button = findViewById<Button>(R.id.loadBtn)
        val resultLayout = findViewById<LinearLayout>(R.id.resultContainer)

        button.setOnClickListener {
            title.text = "Universidades de República Dominicana"
            resultLayout.removeAllViews()

            thread {
                try {
                    val url = URL("https://adamix.net/proxy.php?country=Dominican+Republic")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonArray = JSONArray(response)

                    runOnUiThread {
                        for (i in 0 until jsonArray.length()) {
                            val uni = jsonArray.getJSONObject(i)
                            val name = uni.getString("name")
                            val web = uni.getJSONArray("web_pages").optString(0, "Sin web")

                            val item = TextView(this).apply {
                                text = "🎓 $name\n🌐 $web"
                                textSize = 16f
                                setPadding(0, 16, 0, 16)
                            }
                            resultLayout.addView(item)
                        }
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}