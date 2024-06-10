package com.samz.githubreposviewer.presenter.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samz.githubreposviewer.R
import com.samz.githubreposviewer.domain.model.RepoDetails
import com.samz.githubreposviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.samz.githubreposviewer.presenter.base.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(private val useCase: GetRepositoryDetailsUseCase) :
    ViewModel() {
    private val mutableState: MutableStateFlow<DataState<RepoDetails>> =
        MutableStateFlow(
            DataState(
                isLoading = true
            )
        )
    val state: StateFlow<DataState<RepoDetails>> = mutableState

    fun loadRepoDetails(user: String?, repo: String?):Job {
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
                .onSuccess { details ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            data = details
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