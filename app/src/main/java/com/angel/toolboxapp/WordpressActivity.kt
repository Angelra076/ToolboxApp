package com.angel.toolboxapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class WordpressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordpress)

        val logo = findViewById<ImageView>(R.id.wordpressLogo)
        val container = findViewById<LinearLayout>(R.id.postContainer)
        val loadBtn = findViewById<Button>(R.id.loadPostsBtn)

        // Cargar logo de TechCrunch
        Glide.with(this).load("https://techcrunch.com/wp-content/uploads/2019/06/TechCrunch_logo.png").into(logo)

        loadBtn.setOnClickListener {
            container.removeAllViews()

            thread {
                try {
                    val url = URL("https://techcrunch.com/wp-json/wp/v2/posts")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonArray = JSONArray(response)

                    runOnUiThread {
                        for (i in 0 until minOf(3, jsonArray.length())) {
                            val post = jsonArray.getJSONObject(i)
                            val title = post.getJSONObject("title").getString("rendered")
                            val excerpt = post.getJSONObject("excerpt").getString("rendered")
                                .replace(Regex("<.*?>"), "") // quitar HTML
                            val link = post.getString("link")

                            val titleView = TextView(this).apply {
                                text = "📰 $title"
                                textSize = 18f
                                setPadding(0, 16, 0, 4)
                            }

                            val excerptView = TextView(this).apply {
                                text = excerpt
                                textSize = 14f
                                setPadding(0, 0, 0, 8)
                            }

                            val visitBtn = Button(this).apply {
                                text = "Visitar noticia"
                                setOnClickListener {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                                }
                            }

                            container.addView(titleView)
                            container.addView(excerptView)
                            container.addView(visitBtn)
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