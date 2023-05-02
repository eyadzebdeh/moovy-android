package com.apptasya.moovy.presentation

import androidx.navigation.NavController
import com.apptasya.moovy.presentation.destinations.GenresScreenDestination
import com.apptasya.moovy.presentation.destinations.MoviesScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class MoovyNavigator(
    private val navController: NavController
) {

    fun navigateToGenres(allowPopUp: Boolean = false) {
        navController.navigate(GenresScreenDestination()) {
            if (!allowPopUp) {
                popUpTo(
                    route = navController.graph.route ?: "",
                    popUpToBuilder = { inclusive = false })
            }
        }
    }

    fun navigateToLikes() {
        //TODO: go to likes screen
    }

    fun navigateToMovies() {
        if (navController.backQueue.size > 0) {
            navController.popBackStack()
        } else {
            navController.navigate(MoviesScreenDestination())
        }
    }
}