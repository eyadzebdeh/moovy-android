package com.apptasya.moovy.presentation.movies.model

data class MovieUiElement(
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double,
    val genres: List<String>
)
