package com.apptasya.moovy.presentation.genres.mapper

import com.apptasya.moovy.domain.model.Genre
import com.apptasya.moovy.presentation.genres.model.GenreUiElement

fun Genre.toGenreUiElement(): GenreUiElement {
    return GenreUiElement(
        id = this.id,
        name = this.name,
        photo = this.photo,
        isSelected = this.isSelected
    )
}

fun List<Genre>.toGenreUiElementList(): List<GenreUiElement> {
    return this.map { it.toGenreUiElement() }
}