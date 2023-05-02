package com.apptasya.moovy.data.model

import com.google.gson.annotations.SerializedName

data class SetGenresRequest(
    @SerializedName("genres") val genres: List<Int>,
)