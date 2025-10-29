package com.angel.toolboxapp

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GenderActivity : AppCompatActivity() {

    // Interfaz de Retrofit
    interface GenderApi {
        @GET("/")
        fun getGender(@Query("name") name: String): Call<GenderResponse>
    }

    // Modelo de respuesta
    data class GenderResponse(
        val name: String,
        val gender: String?,
        val probability: Float,
        val count: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val checkBtn = findViewById<Button>(R.id.checkGenderBtn)
        val resultText = findViewById<TextView>(R.id.genderResult)
        val layout = findViewById<LinearLayout>(R.id.genderLayout) // ← cambio aquí

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.genderize.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GenderApi::class.java)

        checkBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Escribe un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            api.getGender(name).enqueue(object : Callback<GenderResponse> {
                override fun onResponse(call: Call<GenderResponse>, response: Response<GenderResponse>) {
                    val gender = response.body()?.gender
                    if (gender != null) {
                        resultText.text = "Género: $gender"
                        layout.setBackgroundColor(if (gender == "male") Color.BLUE else Color.MAGENTA)
                    } else {
                        resultText.text = "No se pudo determinar el género"
                        layout.setBackgroundColor(Color.GRAY)
                    }
                }

                override fun onFailure(call: Call<GenderResponse>, t: Throwable) {
                    resultText.text = "Error: ${t.message}"
                    layout.setBackgroundColor(Color.RED)
                }
            })
        }
    }
}