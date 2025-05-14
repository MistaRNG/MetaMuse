package com.example.metamuse.domain.usecase

import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import javax.inject.Inject

class GetMuseumObjectUseCase @Inject constructor(
    private val repository: MetaMuseRepository
) {
    suspend operator fun invoke(id: Int): MuseumObject {
        return repository.getMuseumObject(id)
    }
}