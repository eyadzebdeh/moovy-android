package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.domain.model.MovieAction
import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMovieActionsUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(action: String): Result<List<MovieAction>> =
        withContext(Dispatchers.IO) {
            movieRepository.getMovieActions(action)
        }
}