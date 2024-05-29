package fr.epsi.baptiste_remi_nicolas.rickandmorty

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("character")
    fun getCharacters(): Call<CharacterResponse>
}

data class CharacterResponse(
    val results: List<Character>
)