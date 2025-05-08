package com.example.metamuse.ui.details

import androidx.lifecycle.ViewModel
import com.example.metamuse.data.MetaMuseRepository
import com.example.metamuse.data.model.MuseumObject

class DetailViewModel(private val metaMuseRepository: MetaMuseRepository) : ViewModel() {
    var museumObject: MuseumObject? = null
}