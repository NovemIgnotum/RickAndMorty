package network

import fr.epsi.baptiste_remi_nicolas.rickandmorty.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RickAndMortyApiService {
    val api: API by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}