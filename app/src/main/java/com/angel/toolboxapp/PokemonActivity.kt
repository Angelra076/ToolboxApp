package com.angel.toolboxapp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PokemonActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        val input = findViewById<EditText>(R.id.pokemonNameInput)
        val button = findViewById<Button>(R.id.searchPokemonBtn)
        val image = findViewById<ImageView>(R.id.pokemonImage)
        val experience = findViewById<TextView>(R.id.pokemonExperience)
        val abilities = findViewById<TextView>(R.id.pokemonAbilities)
        val soundBtn = findViewById<Button>(R.id.playSoundBtn)

        var currentSoundUrl: String? = null

        button.setOnClickListener {
            val name = input.text.toString().trim().lowercase()
            if (name.isEmpty()) {
                Toast.makeText(this, "Escribe el nombre de un Pokémon", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            experience.text = ""
            abilities.text = ""
            image.setImageDrawable(null)
            currentSoundUrl = null

            thread {
                try {
                    val url = URL("https://pokeapi.co/api/v2/pokemon/$name")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    val response = connection.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)

                    val baseExp = json.getInt("base_experience")
                    val abilityArray = json.getJSONArray("abilities")
                    val abilityList = mutableListOf<String>()
                    for (i in 0 until abilityArray.length()) {
                        val ability = abilityArray.getJSONObject(i)
                            .getJSONObject("ability")
                            .getString("name")
                        abilityList.add(ability)
                    }

                    val imageUrl = json.getJSONObject("sprites")
                        .getJSONObject("other")
                        .getJSONObject("official-artwork")
                        .getString("front_default")

                    // Sound URL (custom logic: use GitHub repo or fallback)
                    currentSoundUrl = "https://play.pokemonshowdown.com/audio/cries/$name.mp3"

                    runOnUiThread {
                        experience.text = "Experiencia base: $baseExp"
                        abilities.text = "Habilidades: ${abilityList.joinToString(", ")}"
                        Glide.with(this).load(imageUrl).into(image)
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        soundBtn.setOnClickListener {
            currentSoundUrl?.let {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(it)
                    prepare()
                    start()
                }
            } ?: Toast.makeText(this, "Primero busca un Pokémon", Toast.LENGTH_SHORT).show()
        }
    }
}