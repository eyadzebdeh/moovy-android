package com.apptasya.moovy.data.model

import com.google.gson.annotations.SerializedName

data class MovieActionDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("movieId")
    val movieId: Int,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("date")
    val date: String
)