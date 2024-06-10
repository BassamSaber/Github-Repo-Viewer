package com.samz.githubreposviewer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.samz.githubreposviewer.data.datasource.LocalDatasource
import com.samz.githubreposviewer.data.datasource.RemoteDatasource
import com.samz.githubreposviewer.data.db.entity.RepoEntity
import com.samz.githubreposviewer.data.toRepoEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val localDatasource: LocalDatasource,
    private val remoteDatasource: RemoteDatasource
) : RemoteMediator<Int, RepoEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult =
        try {
            val repos = remoteDatasource.loadRepositories().getOrThrow()
            repos.map { it.toRepoEntity() }.let {
                localDatasource.insertRepositories(it)
            }
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

}