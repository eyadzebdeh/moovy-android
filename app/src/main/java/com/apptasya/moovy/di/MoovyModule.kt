package com.apptasya.moovy.di

import com.apptasya.moovy.data.TMDBWebService
import com.apptasya.moovy.data.repository.MovieRepositoryImpl
import com.apptasya.moovy.domain.repository.MovieRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoovyModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor(requestInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideWebService(client: OkHttpClient): TMDBWebService {
        val gson = GsonBuilder().create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        return Retrofit.Builder()
            .baseUrl("https://us-central1-moovy-173104.cloudfunctions.net/api/")
            .addConverterFactory(gsonConverterFactory).client(client).build().create()
    }


    @Provides
    @Singleton
    fun provideRepository(
        webService: TMDBWebService
    ): MovieRepository {
        return MovieRepositoryImpl(webService)
    }
}