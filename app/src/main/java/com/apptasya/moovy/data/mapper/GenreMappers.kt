package com.apptasya.moovy.data.mapper

import com.apptasya.moovy.data.model.GenreDto
import com.apptasya.moovy.data.model.GenresResponse
import com.apptasya.moovy.domain.model.Genre

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = this.id ?: 0,
        name = this.name ?: "",
        photo = this.photo ?: "",
        isSelected = this.isSelected ?: false
    )
}

fun List<GenreDto>.toGenreList(): List<Genre> {
    return this.map { it.toGenre() }
}

fun GenresResponse.toGenreList(): List<Genre> {
    return this.genres?.toGenreList() ?: emptyList()
}