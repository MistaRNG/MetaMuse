package com.example.metamuse.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metamuse.domain.model.MuseumObject
import com.example.metamuse.domain.usecase.GetInitialMuseumObjectsUseCase
import com.example.metamuse.domain.usecase.LoadMoreMuseumObjectsUseCase
import com.example.metamuse.domain.usecase.SearchMuseumObjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getInitialMuseumObjectsUseCase: GetInitialMuseumObjectsUseCase,
    private val loadMoreMuseumObjectsUseCase: LoadMoreMuseumObjectsUseCase,
    private val searchMuseumObjectsUseCase: SearchMuseumObjectsUseCase,
) : ViewModel() {

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
    private var allIDs = listOf<Int>()
    private var loadedItemCount = 0
    private var currentJobToken = 0

    init {
        observeSearchQuery()
        loadInitial()
    }

    private fun notifyNetworkError() {
        _showNetworkErrorSnackbar.value = true
    }

    fun clearNetworkErrorEvent() {
        _showNetworkErrorSnackbar.value = false
    }

    private fun loadInitial() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val (objects, ids) = getInitialMuseumObjectsUseCase(batchSize = 20)
                allIDs = ids
                allMuseumObjects = objects
                loadedItemCount = objects.size
                _museUiState.value = allMuseumObjects
                _showInitialLoadError.value = false
            } catch (e: Exception) {
                _showInitialLoadError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreMuseumObjects() {
        if (_isLoading.value || _searchMode.value) return
        _isLoading.value = true
        val tokenAtStart = currentJobToken

        viewModelScope.launch {
            try {
                val newObjects = loadMoreMuseumObjectsUseCase(allIDs, loadedItemCount, batchSize = 20)
                if (tokenAtStart != currentJobToken) return@launch
                loadedItemCount += newObjects.size
                allMuseumObjects += newObjects
                _museUiState.value = allMuseumObjects
            } catch (e: Exception) {
                notifyNetworkError()
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _searchMode.value = false
                        _museUiState.value = allMuseumObjects
                        return@collect
                    }

                    _searchMode.value = true
                    _isLoading.value = true

                    try {
                        val results = searchMuseumObjectsUseCase(query)
                        _museUiState.value = results
                    } catch (e: Exception) {
                        notifyNetworkError()
                        _museUiState.value = emptyList()
                    } finally {
                        _isLoading.value = false
                    }
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun retryLastAction() {
        _showInitialLoadError.value = false
        _showNetworkErrorSnackbar.value = false

        if (_searchMode.value) {
            onSearchQueryChange(_searchQuery.value)
        } else {
            if (allIDs.isEmpty()) {
                loadInitial()
            } else {
                loadMoreMuseumObjects()
            }
        }
    }
}
