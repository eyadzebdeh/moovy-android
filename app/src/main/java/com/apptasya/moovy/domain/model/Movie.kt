package com.apptasya.moovy.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double,
    val genres: List<String>
)