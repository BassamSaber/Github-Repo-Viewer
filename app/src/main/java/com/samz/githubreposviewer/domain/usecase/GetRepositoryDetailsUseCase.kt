package com.samz.githubreposviewer.domain.usecase

import com.samz.githubreposviewer.domain.model.RepoDetails
import com.samz.githubreposviewer.domain.repository.ReposRepository

class GetRepositoryDetailsUseCase(private val repository: ReposRepository) {
    suspend fun execute(owner: String, repo: String): Result<RepoDetails?> =
        repository.getRepositoryDetails(owner, repo)
}