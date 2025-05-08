package com.example.metamuse.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metamuse.data.MetaMuseRepository
import com.example.metamuse.data.model.MuseumObject
import kotlinx.coroutines.launch

class SearchViewModel(private val metaMuseRepository: MetaMuseRepository) : ViewModel() {

    var museUiState by mutableStateOf<List<MuseumObject>>(emptyList())
        private set

    private var allMuseumObjects = listOf<MuseumObject>()

    var searchQuery by mutableStateOf("")
        private set

    var loadedItemCount by mutableStateOf(0)
        private set

    val loadBatchSize = 20
    private var allIDs = listOf<Int>()

    var isLoading by mutableStateOf(false)
        private set

    var searchMode by mutableStateOf(false)
        private set

    var showInitialLoadError by mutableStateOf(false)
        private set

    var showNetworkErrorSnackbar by mutableStateOf(false)
        private set

    private var currentJobToken = 0
    private var lastSearchQuery: String? = null

    init {
        getMuseumIDs()
    }

    private fun notifyNetworkError() {
        showNetworkErrorSnackbar = true
    }

    fun clearNetworkErrorEvent() {
        showNetworkErrorSnackbar = false
    }

    private fun getMuseumIDs() {
        viewModelScope.launch {
            try {
                allIDs = metaMuseRepository.getMuseumIDs()
                showInitialLoadError = false
                loadMoreMuseumObjects()
            } catch (e: Exception) {
                showInitialLoadError = true
            }
        }
    }

    fun loadMoreMuseumObjects() {
        if (isLoading || searchMode) return
        isLoading = true
        val tokenAtStart = currentJobToken

        viewModelScope.launch {
            var networkFailure = false
            try {
                val nextBatch = allIDs.drop(loadedItemCount).take(loadBatchSize)
                val newObjects = nextBatch.mapNotNull { id ->
                    try {
                        metaMuseRepository.getMuseumObject(id)
                    } catch (e: Exception) {
                        networkFailure = true
                        null
                    }
                }

                if (tokenAtStart != currentJobToken) return@launch

                loadedItemCount += newObjects.size
                allMuseumObjects += newObjects
                museUiState = allMuseumObjects

                if (networkFailure) notifyNetworkError()
            } finally {
                if (tokenAtStart == currentJobToken) isLoading = false
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        if (newQuery == searchQuery && museUiState.isNotEmpty()) return

        searchQuery = newQuery
        lastSearchQuery = newQuery
        currentJobToken++

        if (newQuery.isBlank()) {
            searchMode = false
            museUiState = allMuseumObjects
            return
        }

        searchMode = true
        viewModelScope.launch {
            isLoading = true
            val tokenAtStart = currentJobToken

            try {
                val searchIDs = metaMuseRepository.searchMuseumObjectIDs(newQuery)

                val searchObjects = searchIDs.take(50).mapNotNull { id ->
                    try {
                        metaMuseRepository.getMuseumObject(id)
                    } catch (e: Exception) {
                        null
                    }
                }

                if (tokenAtStart != currentJobToken) return@launch

                museUiState = searchObjects

            } catch (e: Exception) {
                if (tokenAtStart == currentJobToken) {
                    museUiState = emptyList()
                    notifyNetworkError()
                }
            } finally {
                if (tokenAtStart == currentJobToken) isLoading = false
            }
        }
    }

    fun retryLastAction() {
        showInitialLoadError = false
        showNetworkErrorSnackbar = false

        if (searchMode) {
            onSearchQueryChange(searchQuery)
        } else {
            if (allIDs.isEmpty()) {
                getMuseumIDs()
            } else {
                loadMoreMuseumObjects()
            }
        }
    }
}
