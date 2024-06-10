package com.samz.githubreposviewer.domain.repository

import androidx.paging.PagingData
import com.samz.githubreposviewer.data.db.entity.RepoEntity
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.domain.model.RepoDetails
import kotlinx.coroutines.flow.Flow

interface ReposRepository {
    fun getRepositoriesPagerFlow(): Flow<PagingData<RepoEntity>>

    suspend fun getRepositories(): Result<List<Repo>>

    suspend fun getRepositoryDetails(owner: String, repo: String): Result<RepoDetails?>

    suspend fun getRepositoryIssues(owner: String, repo: String): Result<List<Issue>>
}