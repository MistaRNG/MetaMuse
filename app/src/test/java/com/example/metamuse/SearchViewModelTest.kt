package com.example.metamuse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.metamuse.domain.model.MuseumObject
import com.example.metamuse.domain.usecase.GetInitialMuseumObjectsUseCase
import com.example.metamuse.domain.usecase.LoadMoreMuseumObjectsUseCase
import com.example.metamuse.domain.usecase.SearchMuseumObjectsUseCase
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
    private lateinit var getInitialMuseumObjectsUseCase: GetInitialMuseumObjectsUseCase
    private lateinit var loadMoreMuseumObjectsUseCase: LoadMoreMuseumObjectsUseCase
    private lateinit var searchMuseumObjectsUseCase: SearchMuseumObjectsUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getInitialMuseumObjectsUseCase = mockk()
        loadMoreMuseumObjectsUseCase = mockk()
        searchMuseumObjectsUseCase = mockk()
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
            id = id,
            title = title,
            artistBio = "Bio for $title",
            artist = "Artist $title",
            dimensions = "Dimensions",
            medium = "Medium",
            date = "Date",
            url = "https://example.com",
            imageUrl = "https://image.com/image.jpg",
            additionalImages = listOf("https://image.com/image.jpg")
        )
    }

    @Test
    fun onSearchQueryChange_validQuery_updatesStateWithResults() = runTest {
        val mockObject = createMockMuseumObject(1, "TestTitle")
        coEvery { searchMuseumObjectsUseCase("test") } returns listOf(mockObject)
        coEvery { getInitialMuseumObjectsUseCase(any()) } returns Pair(emptyList(), emptyList())

        viewModel = SearchViewModel(
            getInitialMuseumObjectsUseCase,
            loadMoreMuseumObjectsUseCase,
            searchMuseumObjectsUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("test")
        testScheduler.advanceUntilIdle()

        assertEquals("test", viewModel.searchQuery.value)
        assertEquals(1, viewModel.museUiState.value.size)
        assertEquals("TestTitle", viewModel.museUiState.value.first().title)
    }

    @Test
    fun retryLastAction_inSearchMode_triggersSearchAgain() = runTest {
        val mockObject = createMockMuseumObject(2, "RetryTest")
        coEvery { searchMuseumObjectsUseCase("retry") } returns listOf(mockObject)
        coEvery { getInitialMuseumObjectsUseCase(any()) } returns Pair(emptyList(), emptyList())

        viewModel = SearchViewModel(
            getInitialMuseumObjectsUseCase,
            loadMoreMuseumObjectsUseCase,
            searchMuseumObjectsUseCase
        )

        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChange("retry")
        testScheduler.advanceUntilIdle()

        viewModel.retryLastAction()
        testScheduler.advanceUntilIdle()

        assertEquals("retry", viewModel.searchQuery.value)
        assertEquals(1, viewModel.museUiState.value.size)
        assertEquals("RetryTest", viewModel.museUiState.value.first().title)
    }

    @Test
    fun loadMoreMuseumObjects_whenScrolled_addsNewItems() = runTest {
        val initialObjects = listOf(
            createMockMuseumObject(1, "Obj 1"),
            createMockMuseumObject(2, "Obj 2")
        )
        val nextObjects = listOf(
            createMockMuseumObject(3, "Obj 3"),
            createMockMuseumObject(4, "Obj 4")
        )

        coEvery { getInitialMuseumObjectsUseCase(any()) } returns Pair(initialObjects, listOf(1, 2, 3, 4))
        coEvery { loadMoreMuseumObjectsUseCase(listOf(1, 2, 3, 4), 2, any()) } returns nextObjects
        coEvery { searchMuseumObjectsUseCase(any()) } returns emptyList()

        viewModel = SearchViewModel(
            getInitialMuseumObjectsUseCase,
            loadMoreMuseumObjectsUseCase,
            searchMuseumObjectsUseCase
        )

        testScheduler.advanceUntilIdle()
        viewModel.loadMoreMuseumObjects()
        testScheduler.advanceUntilIdle()

        val result = viewModel.museUiState.value
        assertEquals(4, result.size)
        assertEquals("Obj 1", result[0].title)
        assertEquals("Obj 4", result[3].title)
    }
}