package com.samz.githubreposviewer.domain.usecase

import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.repository.ReposRepository

class GetRepositoryIssuesUseCase(private val repository: ReposRepository) {
    suspend fun execute(owner: String, repo: String): Result<List<Issue>> =
        repository.getRepositoryIssues(owner, repo)
}