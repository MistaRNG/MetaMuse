package com.example.metamuse.domain.usecase

import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import javax.inject.Inject

class SearchMuseumObjectsUseCase @Inject constructor(
    private val repository: MetaMuseRepository
) {
    suspend operator fun invoke(query: String, limit: Int = 50): List<MuseumObject> {
        return repository.searchMuseumObjects(query, limit)
    }
}