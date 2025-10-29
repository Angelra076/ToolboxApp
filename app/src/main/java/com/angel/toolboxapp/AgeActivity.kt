package com.angel.toolboxapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class AgeActivity : AppCompatActivity() {

    interface AgeApi {
        @GET("/")
        fun getAge(@Query("name") name: String): Call<AgeResponse>
    }

    data class AgeResponse(
        val name: String,
        val age: Int?,
        val count: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val checkBtn = findViewById<Button>(R.id.checkAgeBtn)
        val resultText = findViewById<TextView>(R.id.ageResult)
        val ageImage = findViewById<ImageView>(R.id.ageImage)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.agify.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(AgeApi::class.java)

        checkBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Escribe un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            api.getAge(name).enqueue(object : Callback<AgeResponse> {
                override fun onResponse(call: Call<AgeResponse>, response: Response<AgeResponse>) {
                    val age = response.body()?.age
                    if (age != null) {
                        resultText.text = "Edad estimada: $age años"
                        when {
                            age < 30 -> {
                                ageImage.setImageResource(R.drawable.young)
                                resultText.append("\nEstado: Joven")
                            }
                            age in 30..60 -> {
                                ageImage.setImageResource(R.drawable.adult)
                                resultText.append("\nEstado: Adulto")
                            }
                            else -> {
                                ageImage.setImageResource(R.drawable.old)
                                resultText.append("\nEstado: Anciano")
                            }
                        }
                    } else {
                        resultText.text = "No se pudo determinar la edad"
                        ageImage.setImageDrawable(null)
                    }
                }

                override fun onFailure(call: Call<AgeResponse>, t: Throwable) {
                    resultText.text = "Error: ${t.message}"
                    ageImage.setImageDrawable(null)
                }
            })
        }
    }
}