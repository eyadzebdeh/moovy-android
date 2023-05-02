package com.apptasya.moovy.data.mapper

import com.apptasya.moovy.data.model.MovieActionDto
import com.apptasya.moovy.domain.model.MovieAction

fun MovieActionDto.toMovieAction(): MovieAction {
    return MovieAction(
        id = this.id,
        action = this.action,
        movieId = this.movieId,
        userId = this.userId,
        date = this.date
    )
}

fun List<MovieActionDto>.toMovieActionList(): List<MovieAction> {
    return this.map { it.toMovieAction() }
}