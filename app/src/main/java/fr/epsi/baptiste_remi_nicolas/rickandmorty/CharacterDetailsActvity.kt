package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import network.RickAndMortyApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val characterId = intent.getIntExtra("character_id", -1)
        if (characterId != -1) {
            fetchCharacterDetails(characterId)
        } else {
            showError("ID de personnage invalide.")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

                private fun fetchCharacterDetails(characterId: Int) {
                    RickAndMortyApiService.api.getCharacter(characterId)
                        .enqueue(object : Callback<Character> {
                            override fun onResponse(
                                call: Call<Character>,
                                response: Response<Character>
                            ) {
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

                    supportActionBar?.title = character.name
                }

                private fun showError(errorMessage: String) {
                    Toast.makeText(this@CharacterDetailsActivity, errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    findViewById<ScrollView>(R.id.characters_rv).visibility = View.GONE
                    findViewById<TextView>(R.id.errorMessage).apply {
                        visibility = View.VISIBLE
                        text = errorMessage
                    }


                }

            }