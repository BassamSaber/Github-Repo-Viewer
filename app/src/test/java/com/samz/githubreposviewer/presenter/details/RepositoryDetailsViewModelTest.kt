package com.samz.githubreposviewer.presenter.details

import app.cash.turbine.test
import com.samz.githubreposviewer.awaitItemPrecededBy
import com.samz.githubreposviewer.domain.repository.ReposRepository
import com.samz.githubreposviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.samz.githubreposviewer.expectRepositoryDetailModel
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
class RepositoryDetailsViewModelTest {

    private val githubRepository: ReposRepository = mockk()
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase =
        GetRepositoryDetailsUseCase(githubRepository)

    private val viewModel = RepositoryDetailsViewModel(getRepositoryDetailsUseCase)

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
            viewModel.loadRepoDetails(null, "repo").join()

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
            viewModel.loadRepoDetails("user", null).join()

            assertEquals(
                DataState<Any>(error = "User Name or Repository Name is missing."),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Get correct Repository Details`() = runBlocking {
        coEvery {
            githubRepository.getRepositoryDetails("user", "repo")
        } returns Result.success(expectRepositoryDetailModel)

        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadRepoDetails("user", "repo").join()

            assertEquals(
                DataState(data = expectRepositoryDetailModel),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }

    @Test
    fun `Error in API`() = runBlocking {
        coEvery {
            githubRepository.getRepositoryDetails("user", "repo")
        } returns Result.failure(Exception("TestErrorInAPI"))

        viewModel.state.test {
            assertEquals(
                DataState<Any>(isLoading = true),
                awaitItemPrecededBy(DataState(isLoading = false))
            )
            expectNoEvents()
            viewModel.loadRepoDetails("user", "repo").join()

            assertEquals(
                DataState<Any>(error = "TestErrorInAPI"),
                awaitItemPrecededBy(DataState(isLoading = true))
            )
        }
    }
}