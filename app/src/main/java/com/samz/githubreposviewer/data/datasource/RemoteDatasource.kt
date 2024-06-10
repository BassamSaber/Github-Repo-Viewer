package com.samz.githubreposviewer.data.datasource

import com.samz.githubreposviewer.data.api.model.IssueDto
import com.samz.githubreposviewer.data.api.model.RepoDto
import retrofit2.Response

interface RemoteDatasource {
    suspend fun loadRepositories():Result<List<RepoDto>>

    suspend fun loadRepositoryDetails(
        ownerName: String, repoName: String
    ):Result<RepoDto>

    suspend fun loadRepositoryIssues(
        ownerName: String, repoName: String
    ):Result<List<IssueDto>>
}