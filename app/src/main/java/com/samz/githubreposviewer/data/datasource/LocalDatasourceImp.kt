package com.samz.githubreposviewer.data.datasource

import androidx.paging.PagingSource
import com.samz.githubreposviewer.data.db.RepoDao
import com.samz.githubreposviewer.data.db.entity.RepoEntity

class LocalDatasourceImp(
    private val repoDao: RepoDao
) : LocalDatasource {

    override suspend fun insertRepositories(repos: List<RepoEntity>) =
        repoDao.insertAll(repos)

    override fun repoPagingSource(): PagingSource<Int, RepoEntity> =
        repoDao.pagingSource()

    override suspend fun getRepositoryDetails(id: Int): RepoEntity =
        repoDao.getRepoDetails(id)

    override suspend fun clearAll() =
        repoDao.clearAll()
}