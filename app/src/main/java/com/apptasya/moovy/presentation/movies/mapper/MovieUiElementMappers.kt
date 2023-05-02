package com.apptasya.moovy.presentation.movies.mapper

import com.apptasya.moovy.domain.model.Movie
import com.apptasya.moovy.presentation.movies.model.MovieUiElement

fun Movie.toMovieUiElement(): MovieUiElement {
    return MovieUiElement(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate.split("-")[0],
        overview = this.overview,
        voteAverage = this.voteAverage,
        genres = this.genres.take(4)
    )
}

fun List<Movie>.toMovieUiElementList(): List<MovieUiElement> {
    return this.map { it.toMovieUiElement() }
}