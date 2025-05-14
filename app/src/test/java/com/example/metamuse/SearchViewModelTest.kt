package com.example.metamuse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.data.model.MuseumObject
import com.example.metamuse.ui.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MetaMuseRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createMockMuseumObject(
        id: Int,
        title: String = "DefaultTitle"
    ): MuseumObject {
        return MuseumObject(
            objectID = id,
            title = title,
            artistDisplayBio = "Bio for $title",
            artistDisplayName = "Artist $title",
            dimensions = "Dimensions",
            medium = "Medium",
            objectDate = "Date",
            objectURL = "https://example.com",
            primaryImage = "https://image.com/image.jpg"
        )
    }

    @Test
    fun `onSearchQueryChange updates state with results`() = runTest {
        val mockObject = createMockMuseumObject(1, "TestTitle")
        coEvery { repository.searchMuseumObjectIDs("test") } returns listOf(1)
        coEvery { repository.getMuseumObject(1) } returns mockObject

        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("test")
        testScheduler.advanceUntilIdle()

        assertEquals("test", viewModel.searchQuery)
        assertEquals(1, viewModel.museUiState.size)
        assertEquals("TestTitle", viewModel.museUiState.first().title)
    }

    @Test
    fun `retryLastAction calls search again if in searchMode`() = runTest {
        val mockObject = createMockMuseumObject(2, "RetryTest")
        coEvery { repository.searchMuseumObjectIDs("retry") } returns listOf(2)
        coEvery { repository.getMuseumObject(2) } returns mockObject

        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("retry")
        testScheduler.advanceUntilIdle()

        viewModel.retryLastAction()
        testScheduler.advanceUntilIdle()

        assertEquals("retry", viewModel.searchQuery)
        assertEquals(1, viewModel.museUiState.size)
        assertEquals("RetryTest", viewModel.museUiState.first().title)
    }
}