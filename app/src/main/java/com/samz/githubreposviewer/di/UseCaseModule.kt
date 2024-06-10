package com.samz.githubreposviewer.di

import com.samz.githubreposviewer.domain.repository.ReposRepository
import com.samz.githubreposviewer.domain.usecase.GetRepositoriesUseCase
import com.samz.githubreposviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.samz.githubreposviewer.domain.usecase.GetRepositoryIssuesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetRepositoriesUseCase(repository: ReposRepository): GetRepositoriesUseCase =
        GetRepositoriesUseCase(repository)

    @Provides
    fun provideGetRepositoryDetailsUseCase(repository: ReposRepository): GetRepositoryDetailsUseCase =
        GetRepositoryDetailsUseCase(repository)

    @Provides
    fun provideGetRepositoryIssuesUseCase(repository: ReposRepository): GetRepositoryIssuesUseCase =
        GetRepositoryIssuesUseCase(repository)
}