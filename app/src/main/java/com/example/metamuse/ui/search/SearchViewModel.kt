package com.example.metamuse.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metamuse.data.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val metaMuseRepository: MetaMuseRepository) : ViewModel() {

    private val _museUiState = MutableStateFlow<List<MuseumObject>>(emptyList())
    val museUiState: StateFlow<List<MuseumObject>> = _museUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchMode = MutableStateFlow(false)
    val searchMode: StateFlow<Boolean> = _searchMode.asStateFlow()

    private val _showInitialLoadError = MutableStateFlow(false)
    val showInitialLoadError: StateFlow<Boolean> = _showInitialLoadError.asStateFlow()

    private val _showNetworkErrorSnackbar = MutableStateFlow(false)
    val showNetworkErrorSnackbar: StateFlow<Boolean> = _showNetworkErrorSnackbar.asStateFlow()

    private var allMuseumObjects = listOf<MuseumObject>()
    private var loadedItemCount = 0
    private val loadBatchSize = 20
    private var allIDs = listOf<Int>()
    private var currentJobToken = 0
    private var lastSearchQuery: String? = null

    init {
        getMuseumIDs()
    }

    private fun notifyNetworkError() {
        _showNetworkErrorSnackbar.value = true
    }

    fun clearNetworkErrorEvent() {
        _showNetworkErrorSnackbar.value = false
    }

    private fun getMuseumIDs() {
        viewModelScope.launch {
            try {
                allIDs = metaMuseRepository.getMuseumIDs()
                _showInitialLoadError.value = false
                loadMoreMuseumObjects()
            } catch (e: Exception) {
                _showInitialLoadError.value = true
            }
        }
    }

    fun loadMoreMuseumObjects() {
        if (_isLoading.value || _searchMode.value) return
        _isLoading.value = true
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
                _museUiState.value = allMuseumObjects

                if (networkFailure) notifyNetworkError()
            } finally {
                if (tokenAtStart == currentJobToken) _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        if (newQuery == _searchQuery.value && _museUiState.value.isNotEmpty()) return

        _searchQuery.value = newQuery
        lastSearchQuery = newQuery
        currentJobToken++

        if (newQuery.isBlank()) {
            _searchMode.value = false
            _museUiState.value = allMuseumObjects
            return
        }

        _searchMode.value = true
        viewModelScope.launch {
            _isLoading.value = true
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

                _museUiState.value = searchObjects

            } catch (e: Exception) {
                if (tokenAtStart == currentJobToken) {
                    _museUiState.value = emptyList()
                    notifyNetworkError()
                }
            } finally {
                if (tokenAtStart == currentJobToken) _isLoading.value = false
            }
        }
    }

    fun retryLastAction() {
        _showInitialLoadError.value = false
        _showNetworkErrorSnackbar.value = false

        if (_searchMode.value) {
            onSearchQueryChange(_searchQuery.value)
        } else {
            if (allIDs.isEmpty()) {
                getMuseumIDs()
            } else {
                loadMoreMuseumObjects()
            }
        }
    }
}
