package com.apptasya.moovy.presentation.genres

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptasya.moovy.R
import com.apptasya.moovy.domain.model.UiText
import com.apptasya.moovy.domain.usecase.GetGenresUseCase
import com.apptasya.moovy.domain.usecase.SetGenresUseCase
import com.apptasya.moovy.presentation.genres.mapper.toGenreUiElementList
import com.apptasya.moovy.presentation.genres.model.GenreUiElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase, private val setGenresUseCase: SetGenresUseCase
) : ViewModel() {

    var uiState by mutableStateOf(GenresUiState())
        private set

    private val _uiEffect = MutableSharedFlow<GenresEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        loadGenres()
    }

    fun setGenres() {
        if (uiState.isLoading) return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result =
                setGenresUseCase.invoke(uiState.genres.filter { it.isSelected }.map { it.id })
            result.onSuccess {
                _uiEffect.emit(GenresEffect.SetGenresSuccessEffect)
                uiState = uiState.copy(isLoading = false)
            }
            result.onFailure {
                _uiEffect.emit(GenresEffect.ErrorEffect(UiText.StringResource(R.string.load_genres_failure)))
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = getGenresUseCase.invoke()
            result.onSuccess {
                uiState = uiState.copy(
                    genres = it.toGenreUiElementList(), isLoading = false
                )
            }
            result.onFailure {
                _uiEffect.emit(GenresEffect.ErrorEffect(UiText.StringResource(R.string.load_genres_failure)))
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun onGenrePress(genre: GenreUiElement) {
        val genres = uiState.genres.map {
            if (it.id == genre.id) {
                return@map it.copy(isSelected = !it.isSelected)
            }
            return@map it
        }
        uiState = uiState.copy(
            genres = genres,
            isShowingSaveGenres = genres.count { it.isSelected } > 0
        )
    }
}

data class GenresUiState(
    val genres: List<GenreUiElement> = emptyList(),
    val isShowingSaveGenres: Boolean = false,
    val isLoading: Boolean = false
)

sealed class GenresEffect {
    data class ErrorEffect(val message: UiText) : GenresEffect()
    object SetGenresSuccessEffect : GenresEffect()
}