package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetMovieActionUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(action: String, movieId: Int): Result<Unit> = withContext(
        Dispatchers.IO
    ) {
        movieRepository.setMovieAction(action, movieId)
    }
}
