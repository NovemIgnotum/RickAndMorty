package fr.epsi.baptiste_remi_nicolas.rickandmorty

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    // Chemin pour les personnages
    @GET("character")
    fun getCharacters(@Query("page") page: Int): Call<CharacterResponse>

    // Chemin pour les détails d'un personnage
    @GET("character/{id}")
    fun getCharacter(@Path("id") characterId: Int): Call<Character>
}

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)