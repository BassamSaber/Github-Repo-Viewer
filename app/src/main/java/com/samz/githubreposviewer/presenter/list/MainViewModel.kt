package com.samz.githubreposviewer.presenter.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.samz.githubreposviewer.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var stateApp by mutableStateOf(MainState())

    fun onEvent(event: MainEvent) {
        stateApp = when (event) {
            is MainEvent.ThemeChange -> {
                stateApp.copy(theme = event.theme)
            }

            is MainEvent.OpenDialog -> {
                stateApp.copy(openDialog = event.openDialog)
            }
        }
    }

}

sealed class MainEvent {
    data class ThemeChange(val theme: AppTheme) : MainEvent()
    data class OpenDialog(val openDialog: Boolean) : MainEvent()
}

data class MainState(
    val theme: AppTheme = AppTheme.Default,
    val openDialog: Boolean = false
)