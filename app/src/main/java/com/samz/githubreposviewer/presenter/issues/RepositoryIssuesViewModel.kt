package com.samz.githubreposviewer.presenter.issues

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.usecase.GetRepositoryIssuesUseCase
import com.samz.githubreposviewer.presenter.base.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RepositoryIssuesViewModel @Inject constructor(private val useCase: GetRepositoryIssuesUseCase) :
    ViewModel() {
    private val mutableState: MutableStateFlow<DataState<List<Issue>>> =
        MutableStateFlow(
            DataState(
                isLoading = true
            )
        )
    val state: StateFlow<DataState<List<Issue>>> = mutableState

    fun loadRepoIssues(user: String?, repo: String?):Job {
        mutableState.update { it.copy(isLoading = true) }
        return viewModelScope.launch {
            if (user == null || repo == null) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        error = "User Name or Repository Name is missing."
                    )
                }
                return@launch
            }

            useCase.execute(user, repo)
                .onSuccess { issues ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            data = issues
                        )
                    }
                }
                .onFailure { error ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }
}