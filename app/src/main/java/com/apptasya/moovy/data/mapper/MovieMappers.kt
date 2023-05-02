package com.apptasya.moovy.data.mapper

import com.apptasya.moovy.data.model.MovieDto
import com.apptasya.moovy.data.model.MovieSuggestionsResponse
import com.apptasya.moovy.domain.model.Movie

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = this.id ?: 0,
        title = this.title ?: "",
        posterPath = this.poster_path ?: "",
        releaseDate = this.release_date ?: "",
        overview = this.overview ?: "",
        voteAverage = this.vote_average ?: 0.0,
        genres = this.genres ?: emptyList()
    )
}

fun List<MovieDto>.toMovieList(): List<Movie> {
    return this.map { it.toMovie() }
}

fun MovieSuggestionsResponse.toMovieList(): List<Movie> {
    return this.movies?.toMovieList() ?: emptyList()
}
