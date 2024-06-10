package com.samz.githubreposviewer.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.samz.githubreposviewer.data.toRepo
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRepositoriesUseCase(private var reposRepository: ReposRepository) {
    fun execute(): Flow<PagingData<Repo>> =
        reposRepository.getRepositoriesPagerFlow().map { pagingData ->
            pagingData.map { it.toRepo() }
        }
}