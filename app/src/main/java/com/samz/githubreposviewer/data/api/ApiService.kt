package com.samz.githubreposviewer.data.api

import com.samz.githubreposviewer.data.api.model.RepoDto
import com.samz.githubreposviewer.data.api.model.IssueDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("repositories")
    suspend fun loadRepositories():Response<List<RepoDto>>

    @GET("repos/{owner}/{repo}")
    suspend fun loadRepositoryDetails(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String
    ):Response<RepoDto>

    @GET("repos/{owner}/{repo}/issues")
    suspend fun loadRepositoryIssues(
        @Path("owner") ownerName: String,
        @Path("repo") repoName: String
    ):Response<List<IssueDto>>

    /*@GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") searchKey: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ):List<RepoDto>*/

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}