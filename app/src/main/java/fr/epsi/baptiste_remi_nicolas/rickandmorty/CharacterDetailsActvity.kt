package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import network.RickAndMortyApiService
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_item)

        val characterId = intent.getIntExtra("character_id", -1)
        if (characterId != -1) {
            fetchCharacterDetails(characterId)
        } else {
            showError("ID de personnage invalide.")
        }
    }

    private fun fetchCharacterDetails(characterId: Int) {
        RickAndMortyApiService.api.getCharacter(characterId).enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful) {
                    val character = response.body()
                    if (character != null) {
                        updateUI(character)
                    } else {
                        showError("Impossible de récupérer les détails du personnage.")
                    }
                } else {
                    showError("Impossible de récupérer les détails du personnage.")
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                showError("Impossible de récupérer les détails du personnage.")
            }
        })
    }

    private fun updateUI(character: Character) {
        findViewById<TextView>(R.id.characterName).text = character.name
        findViewById<TextView>(R.id.characterSpecies).text = character.species
        findViewById<TextView>(R.id.characterStatus).text = character.status
        findViewById<TextView>(R.id.characterOrigin).text = character.origin.name
        findViewById<TextView>(R.id.characterLocation).text = character.location.name

        val characterImageView = findViewById<ImageView>(R.id.characterImage)
        characterImageView.load(character.image) {
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
