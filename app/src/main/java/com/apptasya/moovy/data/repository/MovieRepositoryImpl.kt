package com.apptasya.moovy.data.repository

import com.apptasya.moovy.data.TMDBWebService
import com.apptasya.moovy.data.mapper.toGenreList
import com.apptasya.moovy.data.mapper.toMovieActionList
import com.apptasya.moovy.data.mapper.toMovieList
import com.apptasya.moovy.data.mapper.toVideoList
import com.apptasya.moovy.data.model.ErrorResponse
import com.apptasya.moovy.data.model.SetGenresRequest
import com.apptasya.moovy.domain.model.Genre
import com.apptasya.moovy.domain.model.GetMoviesSuggestionsResult
import com.apptasya.moovy.domain.model.MovieAction
import com.apptasya.moovy.domain.model.Video
import com.apptasya.moovy.domain.repository.MovieRepository
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val webService: TMDBWebService
) : MovieRepository {

    override suspend fun getGenres(): Result<List<Genre>> {
        return try {
            val response = webService.getGenres()
            if (response.isSuccessful) {
                response.body()?.let {
                    return Result.success(it.toGenreList())
                } ?: let {
                    return Result.failure(EXCEPTION)
                }
            } else {
                return Result.failure(EXCEPTION)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setGenres(genres: List<Int>): Result<Unit> {
        return try {
            webService.setGenres(SetGenresRequest(genres))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieSuggestions(): GetMoviesSuggestionsResult {
        return try {
            val response = webService.getMovieSuggestions()
            if (response.isSuccessful) {
                GetMoviesSuggestionsResult.Success(response.body()?.toMovieList() ?: emptyList())
            } else {
                val errorResponse = response.errorBody()?.let {
                    Gson().fromJson(it.string(), ErrorResponse::class.java)
                }
                if (errorResponse?.error?.code == 1) {
                    GetMoviesSuggestionsResult.Error(GetMoviesSuggestionsResult.GetMoviesSuggestionsError.NoGenres)
                } else {
                    GetMoviesSuggestionsResult.Error(GetMoviesSuggestionsResult.GetMoviesSuggestionsError.UnknownError)
                }
            }
        } catch (e: Exception) {
            GetMoviesSuggestionsResult.Error(GetMoviesSuggestionsResult.GetMoviesSuggestionsError.UnknownError)
        }
    }

    override suspend fun setMovieAction(action: String, movieId: Int): Result<Unit> {
        return try {
            webService.setMovieAction(action, movieId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieActions(action: String): Result<List<MovieAction>> {
        return try {
            webService.getMovieActions(action).getOrNull()?.toMovieActionList()?.let {
                Result.success(it)
            } ?: Result.failure(EXCEPTION)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVideos(movieId: Int): Result<List<Video>> {
        return try {
            val response = webService.getVideos(movieId)
            if (response.isSuccessful) {
                Result.success(response.body()?.toVideoList() ?: emptyList())
            } else {
                Result.failure(EXCEPTION)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        val EXCEPTION = IOException()
    }
}