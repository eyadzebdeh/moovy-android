package com.apptasya.moovy.data

import com.apptasya.moovy.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface TMDBWebService {

    @GET("genres")
    suspend fun getGenres(): Response<GenresResponse?>

    @POST("genres")
    suspend fun setGenres(@Body genres: SetGenresRequest): Result<Unit>

    @GET("movies")
    suspend fun getMovieSuggestions(): Response<MovieSuggestionsResponse?>

    @POST("movieAction")
    suspend fun setMovieAction(
        @Query("action") action: String,
        @Query("movieId") movieId: Int
    ): Result<Unit>

    @GET("movieActions")
    suspend fun getMovieActions(@Query("action") action: String): Result<List<MovieActionDto>>

    @GET("movies/{movieId}/videos")
    suspend fun getVideos(@Path("movieId") movieId: Int): Response<VideoResponse>



}