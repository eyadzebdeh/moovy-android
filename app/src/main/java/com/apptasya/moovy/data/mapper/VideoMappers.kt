package com.apptasya.moovy.data.mapper

import com.apptasya.moovy.data.model.VideoDto
import com.apptasya.moovy.data.model.VideoResponse
import com.apptasya.moovy.domain.model.Video

fun VideoDto.toVideo(): Video {
    return Video(
        key = this.key ?: "",
    )
}

fun List<VideoDto>.toVideoList(): List<Video> {
    return this.map { it.toVideo() }
}

fun VideoResponse.toVideoList(): List<Video> {
    return this.videos?.toVideoList() ?: emptyList()
}