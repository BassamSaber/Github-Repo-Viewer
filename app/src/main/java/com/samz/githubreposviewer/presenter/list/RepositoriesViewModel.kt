package com.samz.githubreposviewer.presenter.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.domain.usecase.GetRepositoriesUseCase
import com.samz.githubreposviewer.presenter.base.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private var useCase: GetRepositoriesUseCase) : ViewModel() {
    private val mutableState: MutableStateFlow<DataState<List<Repo>>> =
        MutableStateFlow(DataState(isLoading = true))

    val state: StateFlow<DataState<List<Repo>>> = mutableState
    var pagingResult: Flow<PagingData<Repo>> = emptyFlow()

    init {
        loadData()
    }

    private fun loadData() {
        mutableState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                pagingResult = useCase.execute()
                    .cachedIn(viewModelScope)
                mutableState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                mutableState.update { dataState -> dataState.copy(error = e.message) }
            }
        }
    }
}