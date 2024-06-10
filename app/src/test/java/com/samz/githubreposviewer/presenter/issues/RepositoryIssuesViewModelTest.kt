package com.samz.githubreposviewer.presenter.issues

import app.cash.turbine.test
import com.samz.githubreposviewer.awaitItemPrecededBy
import com.samz.githubreposviewer.domain.repository.ReposRepository
import com.samz.githubreposviewer.domain.usecase.GetRepositoryIssuesUseCase
import com.samz.githubreposviewer.expectRepositoryIssues
import com.samz.githubreposviewer.presenter.base.DataState
import io.mockk.coEvery
import io.mockk.mockk
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
class RepositoryIssuesViewModelTest {

    private val githubRepository: ReposRepository = mockk()
    private val getRepositoryIssuesUseCase: GetRepositoryIssuesUseCase =
        GetRepositoryIssuesUseCase(githubRepository)

    private val viewModel = RepositoryIssuesViewModel(getRepositoryIssuesUseCase)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Show loading state on init`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
        }
    }

    @Test
    fun `Do not supply user name`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )

            expectNoEvents()
            viewModel.loadRepoIssues(null, "repo").join()

            assertEquals(
                DataState<Any>(error = "User Name or Repository Name is missing."),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Do not supply repo name`() = runBlocking {
        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )

            expectNoEvents()
            viewModel.loadRepoIssues("user", null).join()

            assertEquals(
                DataState<Any>(error = "User Name or Repository Name is missing."),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Get correct Repository Issues`() = runBlocking {
        coEvery {
            githubRepository.getRepositoryIssues("user", "repo")
        } returns Result.success(expectRepositoryIssues)

        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadRepoIssues("user", "repo").join()

            assertEquals(
                DataState(data = expectRepositoryIssues),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Error in API`() = runBlocking {
        coEvery {
            githubRepository.getRepositoryIssues("user", "repo")
        } returns Result.failure(Exception("TestErrorInAPI"))

        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadRepoIssues("user", "repo").join()

            assertEquals(
                DataState<Any>(error = "TestErrorInAPI"),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }
}