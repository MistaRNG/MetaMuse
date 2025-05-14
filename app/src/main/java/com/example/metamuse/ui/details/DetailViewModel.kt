package com.example.metamuse.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metamuse.domain.model.MuseumObject
import com.example.metamuse.domain.usecase.GetMuseumObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMuseumObjectUseCase: GetMuseumObjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _museumObject = MutableStateFlow<MuseumObject?>(null)
    val museumObject: StateFlow<MuseumObject?> = _museumObject.asStateFlow()

    init {
        val id = savedStateHandle.get<Int>(DetailsDestination.idArg)
        if (id != null) {
            viewModelScope.launch {
                try {
                    _museumObject.value = getMuseumObjectUseCase(id)
                } catch (e: Exception) {
                    _museumObject.value = null
                }
            }
        }
    }
}
