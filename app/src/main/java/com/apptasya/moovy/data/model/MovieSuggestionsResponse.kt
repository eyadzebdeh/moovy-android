package com.apptasya.moovy.data.model

import com.google.gson.annotations.SerializedName

data class MovieSuggestionsResponse(
    @SerializedName("movies")
    val movies: List<MovieDto>?,
    @SerializedName("source")
    val source: String?
)