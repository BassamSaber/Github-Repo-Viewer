package com.samz.githubreposviewer.data.repository

import com.samz.githubreposviewer.data.datasource.LocalDatasource
import com.samz.githubreposviewer.data.datasource.RemoteDatasource
import com.samz.githubreposviewer.data.toIssue
import com.samz.githubreposviewer.data.toRepo
import com.samz.githubreposviewer.data.toRepoDetails
import com.samz.githubreposviewer.expectRepositoryDto
import com.samz.githubreposviewer.expectRepositoryDtoList
import com.samz.githubreposviewer.expectRepositoryIssues
import com.samz.githubreposviewer.expectRepositoryIssuesDtoList
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ReposRepositoryTest {

    private val local: LocalDatasource = mockk()
    private val remote: RemoteDatasource = mockk()
    private val remoteMediator: RepoRemoteMediator = mockk()

    private val repository = ReposRepositoryImp(local, remote, remoteMediator)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Retrieve repositories from remote data source`() = runBlocking {
        coEvery { local.insertRepositories(any()) } just runs
        coEvery {
            remote.loadRepositories()
        } returns Result.success(expectRepositoryDtoList)

        val list = repository.getRepositories().getOrNull()
        assertEquals(list, expectRepositoryDtoList.map { it.toRepo() })
    }

    @Test
    fun `Fail to retrieve repositories from remote data source`() = runBlocking {
        val ex = Exception("No API data")
        coEvery { local.insertRepositories(any()) } just runs
        coEvery {
            remote.loadRepositories()
        } returns Result.failure(ex)

        val exception = repository.getRepositories().exceptionOrNull()
        assertEquals(exception, ex)
    }

    @Test
    fun `Retrieve repository details from remote data source`() = runBlocking {
        coEvery {
            remote.loadRepositoryDetails("user", "repo")
        } returns Result.success(expectRepositoryDto)

        val result = repository.getRepositoryDetails("user", "repo")
        assertEquals(result.getOrThrow(), expectRepositoryDto.toRepoDetails())
    }

    @Test
    fun `Fail to retrieve repository details from remote data source`() = runBlocking {
        val ex = Exception("No API data")
        coEvery {
            remote.loadRepositoryDetails("user", "repo")
        } returns Result.failure(ex)

        val exception = repository.getRepositoryDetails("user", "repo").exceptionOrNull()
        assertEquals(exception, ex)
    }

    @Test
    fun `Retrieve issues from remote data source`() = runBlocking {
        coEvery { local.insertRepositories(any()) } just runs
        coEvery {
            remote.loadRepositoryIssues("user", "repo")
        } returns Result.success(expectRepositoryIssuesDtoList)

        val list = repository.getRepositoryIssues("user", "repo").getOrNull()
        assertEquals(list, expectRepositoryIssuesDtoList.map { it.toIssue() })
    }

    @Test
    fun `Fail to retrieve issues from remote data source`() = runBlocking {
        val ex = Exception("No API data")
        coEvery { local.insertRepositories(any()) } just runs
        coEvery {
            remote.loadRepositories()
        } returns Result.failure(ex)

        val exception = repository.getRepositories().exceptionOrNull()
        assertEquals(exception, ex)
    }
}