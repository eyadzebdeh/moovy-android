package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.domain.model.GetMoviesSuggestionsResult
import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMovieSuggestionsUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): GetMoviesSuggestionsResult = withContext(Dispatchers.IO) {
        movieRepository.getMovieSuggestions()
    }
}
