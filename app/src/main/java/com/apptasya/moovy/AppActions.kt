package com.apptasya.moovy

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppActions @Inject constructor() {
    val actions: MutableSharedFlow<AppAction> = MutableSharedFlow()
}

sealed class AppAction {
    object RefreshMoviesAction : AppAction()
}