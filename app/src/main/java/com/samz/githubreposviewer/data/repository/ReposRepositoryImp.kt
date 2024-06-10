package com.samz.githubreposviewer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.samz.githubreposviewer.data.datasource.LocalDatasource
import com.samz.githubreposviewer.data.datasource.RemoteDatasource
import com.samz.githubreposviewer.data.db.entity.RepoEntity
import com.samz.githubreposviewer.data.toIssue
import com.samz.githubreposviewer.data.toRepo
import com.samz.githubreposviewer.data.toRepoDetails
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.domain.model.RepoDetails
import com.samz.githubreposviewer.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class ReposRepositoryImp(
    private val localDatasource: LocalDatasource,
    private val remoteDatasource: RemoteDatasource,
    private val repoRemoteMediator: RepoRemoteMediator
) : ReposRepository {
    override fun getRepositoriesPagerFlow(): Flow<PagingData<RepoEntity>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = repoRemoteMediator,
            pagingSourceFactory = { localDatasource.repoPagingSource() }
        ).flow

    override suspend fun getRepositories(): Result<List<Repo>> {
        return try {
            val data  = remoteDatasource.loadRepositories().getOrThrow()
            Result.success(data.map { it.toRepo() })
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRepositoryDetails(owner: String, repo: String): Result<RepoDetails?> {
        return try {
            val item = remoteDatasource.loadRepositoryDetails(owner, repo).getOrThrow()
            Result.success(item.toRepoDetails())
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRepositoryIssues(owner: String, repo: String): Result<List<Issue>> {
        return try {
            val issues = remoteDatasource.loadRepositoryIssues(owner, repo).getOrThrow()
            Result.success(issues.map { it.toIssue() })
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}