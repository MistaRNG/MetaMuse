package com.example.metamuse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.metamuse.domain.model.MuseumObject
import com.example.metamuse.domain.usecase.GetMuseumObjectUseCase
import com.example.metamuse.ui.details.DetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getMuseumObjectUseCase: GetMuseumObjectUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMuseumObjectUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createMockMuseumObject(id: Int): MuseumObject {
        return MuseumObject(
            id = id,
            title = "Title",
            artistBio = "Bio for test",
            artist = "Artist for test",
            dimensions = "Dimensions",
            medium = "Medium",
            date = "Date",
            url = "https://example.com",
            imageUrl = "https://image.com/image.jpg",
            additionalImages = listOf("https://image.com/image.jpg")
        )
    }

    @Test
    fun initialization_withValidId_loadsMuseumObjectCorrectly() = runTest {
        val mockId = 1
        val mockMuseumObject = createMockMuseumObject(mockId)
        coEvery { getMuseumObjectUseCase(mockId) } returns mockMuseumObject

        val savedStateHandle = SavedStateHandle(mapOf("id" to mockId))
        val viewModel = DetailViewModel(getMuseumObjectUseCase, savedStateHandle)

        advanceUntilIdle()
        val result = viewModel.museumObject.first()

        assertNotNull(result)
        assertEquals(mockId, result?.id)
        assertEquals("Title", result?.title)
    }
}
