package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.RickAndMortyApiService

class CharactersListActivity : AppCompatActivity() {
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
        recyclerView.adapter = adapter

        fetchCharacters()
    }

    private fun fetchCharacters() {
        RickAndMortyApiService.api.getCharacters().enqueue(object : retrofit2.Callback<CharacterResponse> {
            override fun onResponse(call: retrofit2.Call<CharacterResponse>, response: retrofit2.Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateCharacters(it.results)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<CharacterResponse>, t: Throwable) {
                Log.e("CharactersListActivity", "Failed to fetch characters", t)
            }
        })
    }
}