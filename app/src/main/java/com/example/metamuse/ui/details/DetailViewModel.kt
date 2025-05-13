package com.example.metamuse.ui.details

import androidx.lifecycle.ViewModel
import com.example.metamuse.domain.model.MuseumObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    var museumObject: MuseumObject? = null
}
