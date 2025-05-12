package com.example.metamuse.ui.details

import androidx.lifecycle.ViewModel
import com.example.metamuse.data.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject

class DetailViewModel : ViewModel() {
    var museumObject: MuseumObject? = null
}
