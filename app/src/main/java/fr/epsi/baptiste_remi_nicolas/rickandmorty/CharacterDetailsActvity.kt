package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import network.RickAndMortyApiService

class CharacterDetailsActivity: AppCompatActivity() {
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
        RickAndMortyApiService.api.getCharacter(characterId).enqueue(object : retrofit2.Callback<Character> {
            override fun onResponse(call: retrofit2.Call<Character>, response: retrofit2.Response<Character>) {
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

            override fun onFailure(call: retrofit2.Call<Character>, t: Throwable) {
                showError("Impossible de récupérer les détails du personnage.")
            }
        })
    }

    private fun updateUI(character: Character) {
        // Mettez à jour les TextView avec les données du personnage
        val nameTextView = findViewById<TextView>(R.id.characterName)
        nameTextView.text = character.name
        // Mettez à jour d'autres TextView avec les autres données du personnage
    }

    private fun showError(errorMessage: String) {
        // Affichez un message d'erreur à l'utilisateur
    }
}