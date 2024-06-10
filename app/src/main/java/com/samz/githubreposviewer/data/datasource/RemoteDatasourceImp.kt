package com.samz.githubreposviewer.data.datasource

import com.samz.githubreposviewer.data.api.ApiService
import com.samz.githubreposviewer.data.api.model.IssueDto
import com.samz.githubreposviewer.data.api.model.RepoDto
import retrofit2.Response

class RemoteDatasourceImp(private val apiService: ApiService) : RemoteDatasource {

    private fun <T>handleFailure (response: Response<T>): Result<T> {
        when (response.code()) {
            403 -> return Result.failure(Exception("API rate limit exceeded."))
            404 -> return Result.failure(Exception("Not found."))
            405 -> return Result.failure(Exception("Method not allowed"))
        }

        return Result.failure(Exception("An error occurred while retrieving data"))
    }

    override suspend fun loadRepositories(): Result<List<RepoDto>> {
        val response = apiService.loadRepositories()
        if (response.isSuccessful)
            return Result.success(response.body() ?: emptyList())

        return handleFailure(response)
    }


    override suspend fun loadRepositoryDetails(
        ownerName: String,
        repoName: String
    ): Result<RepoDto> {
        val response = apiService.loadRepositoryDetails(ownerName, repoName)

        if (response.isSuccessful)
            response.body()?.let {
                return  Result.success(it)
            }

        return handleFailure(response)
    }

    override suspend fun loadRepositoryIssues(
        ownerName: String,
        repoName: String
    ): Result<List<IssueDto>> {
        val response = apiService.loadRepositoryIssues(ownerName, repoName)
        if (response.isSuccessful)
            return Result.success(response.body() ?: emptyList())
        return handleFailure(response)
    }
}