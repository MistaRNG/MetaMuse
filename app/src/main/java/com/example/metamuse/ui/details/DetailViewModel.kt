package com.example.metamuse.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metamuse.data.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MetaMuseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _museumObject = MutableStateFlow<MuseumObject?>(null)
    val museumObject: StateFlow<MuseumObject?> = _museumObject.asStateFlow()

    init {
        val id = savedStateHandle.get<Int>(DetailsDestination.idArg)
        if (id != null) {
            viewModelScope.launch {
                try {
                    _museumObject.value = repository.getMuseumObject(id)
                } catch (e: Exception) {
                    _museumObject.value = null
                }
            }
        }
    }
}
