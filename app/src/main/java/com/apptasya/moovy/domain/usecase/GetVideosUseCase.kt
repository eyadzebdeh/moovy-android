package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.domain.model.Video
import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Int): Result<List<Video>> = withContext(
        Dispatchers.IO
    ) {
        movieRepository.getVideos(movieId)
    }
}
