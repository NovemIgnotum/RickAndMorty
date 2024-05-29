package fr.epsi.baptiste_remi_nicolas.rickandmorty

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceRetrofit {
    val api: API by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/character/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}