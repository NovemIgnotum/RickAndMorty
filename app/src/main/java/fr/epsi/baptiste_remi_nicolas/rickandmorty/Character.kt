package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.webkit.WebStorage
import java.util.Objects

data class Character(val id: Int, val name: String, val species: String, val status: String, val gender: String, val origin: Origin, val location: Location, val image: String)

data class Origin(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)
