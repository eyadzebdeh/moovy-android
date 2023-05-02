package com.apptasya.moovy.data.model

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val poster_path: String?,
    @SerializedName("release_date")
    val release_date: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("vote_average")
    val vote_average: Double?,
    @SerializedName("genres")
    val genres: List<String>?
)
