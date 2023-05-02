package com.apptasya.moovy.domain.usecase

import com.apptasya.moovy.AppAction
import com.apptasya.moovy.AppActions
import com.apptasya.moovy.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val appActions: AppActions
) {

    suspend operator fun invoke(genres: List<Int>): Result<Unit> = withContext(Dispatchers.IO) {
        val result = movieRepository.setGenres(genres)
        if (result.isSuccess) {
            appActions.actions.emit(AppAction.RefreshMoviesAction)
        }
        return@withContext result
    }
}