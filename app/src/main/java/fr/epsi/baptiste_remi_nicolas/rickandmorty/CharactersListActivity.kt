package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import network.RickAndMortyApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersListActivity : AppCompatActivity(), CharacterAdapter.OnCharacterClickListener {

    private lateinit var adapter: CharacterAdapter
    private lateinit var loadingLayout: RelativeLayout
    private lateinit var animationStart: ImageView

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

        loadingLayout = findViewById(R.id.loading_view)
        animationStart = findViewById(R.id.animation_start)

        showLoadingLayout() // Afficher l'écran de chargement initial

        fetchCharacters()
    }

    private fun fetchCharacters() {
        val allCharacters = mutableListOf<Character>()
        var currentPage = 1
        var totalPages = 0

        fun fetchCharacters(page: Int) {
            RickAndMortyApiService.api.getCharacters(page).enqueue(object : Callback<CharacterResponse> {
                override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
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
                                hideLoadingLayout()
                            }
                        }
                    } else {
                        showError()
                    }
                }

                override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
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
        findViewById<RelativeLayout>(R.id.errorLayout).apply {
            visibility = View.VISIBLE
        }

        val errorGifImageView = findViewById<ImageView>(R.id.errorGifImageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.failed_animation)
            .skipMemoryCache(true)
            .into(errorGifImageView)

        hideLoadingLayout()
    }

    private fun showRecyclerView() {
        findViewById<RecyclerView>(R.id.characters_rv).visibility = View.VISIBLE
        findViewById<RelativeLayout>(R.id.errorLayout).apply {
            visibility = View.GONE
        }
    }

    private fun showLoadingLayout() {
        loadingLayout.visibility = View.VISIBLE
        Glide.with(this)
            .asGif()
            .load(R.drawable.start_animation)
            .into(animationStart)
    }

    private fun hideLoadingLayout() {
        loadingLayout.visibility = View.GONE
    }
}
