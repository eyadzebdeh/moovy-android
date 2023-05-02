package com.apptasya.moovy.domain.model

sealed class GetMoviesSuggestionsResult {
    data class Success(val movies: List<Movie>) : GetMoviesSuggestionsResult()
    data class Error(val error: GetMoviesSuggestionsError): GetMoviesSuggestionsResult()

    sealed class GetMoviesSuggestionsError{
        object NoGenres: GetMoviesSuggestionsError()
        object UnknownError: GetMoviesSuggestionsError()
    }
}