package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.RickAndMortyApiService

class CharactersListActivity : AppCompatActivity(), CharacterAdapter.OnCharacterClickListener {

    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_characters_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.characters_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CharacterAdapter(mutableListOf())
        adapter.setOnCharacterClickListener(this)
        recyclerView.adapter = adapter

        fetchCharacters()
    }

    private fun fetchCharacters() {
        val allCharacters = mutableListOf<Character>()
        var currentPage = 1
        var totalPages = 0

        fun fetchCharacters(page: Int) {
            RickAndMortyApiService.api.getCharacters(page).enqueue(object : retrofit2.Callback<CharacterResponse> {
                override fun onResponse(call: retrofit2.Call<CharacterResponse>, response: retrofit2.Response<CharacterResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (totalPages == 0) {
                                totalPages = it.info.pages
                            }

                            allCharacters.addAll(it.results)
                            if (page < totalPages) {
                                fetchCharacters(page + 1)
                            } else {
                                adapter.updateCharacters(allCharacters)
                            }
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<CharacterResponse>, t: Throwable) {
                    Log.e("CharactersListActivity", "Failed to fetch characters", t)
                    showError()
                }
            })
        }

        fetchCharacters(currentPage)
    }

    override fun onCharacterClick(character: Character) {
        val intent = Intent(this, CharacterDetailsActivity::class.java).apply {
            putExtra("character_id", character.id)
        }
        startActivity(intent)
    }

    private fun showError() {
        findViewById<RecyclerView>(R.id.characters_rv).visibility = View.GONE
        findViewById<TextView>(R.id.errorMessage).apply {
            visibility = View.VISIBLE
        }
    }
}
