package com.samz.githubreposviewer.di

import com.samz.githubreposviewer.data.api.ApiService
import com.samz.githubreposviewer.data.datasource.LocalDatasource
import com.samz.githubreposviewer.data.datasource.LocalDatasourceImp
import com.samz.githubreposviewer.data.datasource.RemoteDatasource
import com.samz.githubreposviewer.data.datasource.RemoteDatasourceImp
import com.samz.githubreposviewer.data.db.RepoDao
import com.samz.githubreposviewer.data.repository.RepoRemoteMediator
import com.samz.githubreposviewer.data.repository.ReposRepositoryImp
import com.samz.githubreposviewer.domain.repository.ReposRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideLocaleDatasource(repoDao: RepoDao): LocalDatasource =
        LocalDatasourceImp(repoDao)

    @Provides
    fun provideRemoteDatasource(apiService: ApiService): RemoteDatasource =
        RemoteDatasourceImp(apiService)

    @Provides
    fun provideRepoRemoteMediator(
        localDatasource: LocalDatasource,
        remoteDatasource: RemoteDatasource
    ): RepoRemoteMediator =
        RepoRemoteMediator(localDatasource, remoteDatasource)

    @Provides
    fun provideReposRepository(
        localDatasource: LocalDatasource,
        remoteDatasource: RemoteDatasource,
        repoRemoteMediator: RepoRemoteMediator
    ): ReposRepository {
        return ReposRepositoryImp(localDatasource, remoteDatasource, repoRemoteMediator)
    }
}