package com.samz.githubreposviewer.data.datasource

import androidx.paging.PagingSource
import com.samz.githubreposviewer.data.db.entity.RepoEntity

interface LocalDatasource {

    suspend fun insertRepositories(repos: List<RepoEntity>)

    fun repoPagingSource(): PagingSource<Int, RepoEntity>

    suspend fun getRepositoryDetails(id: Int): RepoEntity

    suspend fun clearAll()

}