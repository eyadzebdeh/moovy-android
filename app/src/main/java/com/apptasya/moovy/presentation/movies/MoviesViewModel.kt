package com.apptasya.moovy.presentation.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptasya.moovy.AppAction
import com.apptasya.moovy.AppActions
import com.apptasya.moovy.domain.model.GetMoviesSuggestionsResult
import com.apptasya.moovy.domain.usecase.GetMovieSuggestionsUseCase
import com.apptasya.moovy.domain.usecase.GetVideosUseCase
import com.apptasya.moovy.presentation.movies.mapper.toMovieUiElementList
import com.apptasya.moovy.presentation.movies.model.MovieUiElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMovieSuggestionsUseCase: GetMovieSuggestionsUseCase,
    private val getVideosUseCase: GetVideosUseCase,
    private val appActions: AppActions
) : ViewModel() {

    var uiState by mutableStateOf(MoviesUiState())
        private set

    private val _uiEffect = MutableSharedFlow<MoviesEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        loadMovies()
        listenToEvents()
    }

    fun likeMovie() {
        val movie = uiState.movies.getOrNull(0) ?: return
        //TODO: call api
        removeMovie(movie)
    }

    fun unlikeMovie() {
        val movie = uiState.movies.getOrNull(0) ?: return
        //TODO: call api
        removeMovie(movie)
    }

    private fun removeMovie(movie: MovieUiElement) {
        uiState = uiState.copy(movies = uiState.movies.filter { it.id != movie.id })
        loadVideo()
        loadMoreMoviesIfNeeded()
    }

    private fun loadMoreMoviesIfNeeded() {
        if (uiState.movies.size < 3) {
            loadMovies()
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            when (val result = getMovieSuggestionsUseCase.invoke()) {
                is GetMoviesSuggestionsResult.Success -> {
                    uiState = uiState.copy(
                        movies = uiState.movies.toMutableList() + result.movies.toMovieUiElementList(),
                        isLoading = false
                    )
                    loadVideo()
                }
                is GetMoviesSuggestionsResult.Error -> {
                    uiState = uiState.copy(isLoading = false)
                    when (result.error) {
                        GetMoviesSuggestionsResult.GetMoviesSuggestionsError.NoGenres -> {
                            _uiEffect.emit(MoviesEffect.NavigateToGenres)
                        }
                        GetMoviesSuggestionsResult.GetMoviesSuggestionsError.UnknownError -> {
                            _uiEffect.emit(MoviesEffect.UnknownError)
                        }
                    }
                }
            }
        }
    }

    private fun loadVideo() {
        val movieId = uiState.movies.getOrNull(0)?.id ?: return
        viewModelScope.launch {
            val result = getVideosUseCase.invoke(movieId)
            result.getOrNull()?.getOrNull(0)?.key?.let { videoId ->
                uiState = uiState.copy(videoId = videoId)
            }
        }
    }

    private fun listenToEvents() {
        viewModelScope.launch {
            appActions.actions.collect {
                when (it) {
                    is AppAction.RefreshMoviesAction -> {
                        uiState = uiState.copy(movies = emptyList(), videoId = null)
                        loadMovies()
                    }
                    else -> Unit
                }
            }
        }
    }
}

data class MoviesUiState(
    val movies: List<MovieUiElement> = emptyList(),
    val videoId: String? = null,
    val isLoading: Boolean = false
)

sealed class MoviesEffect {
    object NavigateToGenres : MoviesEffect()
    object UnknownError : MoviesEffect()
}