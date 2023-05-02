package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.domain.model.Genre
import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): Result<List<Genre>> = withContext(Dispatchers.IO) {
        movieRepository.getGenres()
    }
}
