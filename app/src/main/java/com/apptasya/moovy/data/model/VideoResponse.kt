package com.apptasya.moovy.data.model

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("videos")
    val videos: List<VideoDto>?,

    )