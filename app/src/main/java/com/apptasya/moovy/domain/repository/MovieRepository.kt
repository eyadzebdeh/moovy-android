package com.apptasya.moovy.domain.repository

import com.apptasya.moovy.domain.model.Genre
import com.apptasya.moovy.domain.model.GetMoviesSuggestionsResult
import com.apptasya.moovy.domain.model.MovieAction
import com.apptasya.moovy.domain.model.Video

interface MovieRepository {

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun setGenres(genres: List<Int>): Result<Unit>

    suspend fun getMovieSuggestions(): GetMoviesSuggestionsResult

    suspend fun setMovieAction(action: String, movieId: Int): Result<Unit>

    suspend fun getMovieActions(action: String): Result<List<MovieAction>>

    suspend fun getVideos(movieId: Int): Result<List<Video>>
}