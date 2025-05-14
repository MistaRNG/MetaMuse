package com.example.metamuse.domain.usecase

import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import javax.inject.Inject

class GetInitialMuseumObjectsUseCase @Inject constructor(
    private val repository: MetaMuseRepository
) {
    suspend operator fun invoke(batchSize: Int): Pair<List<MuseumObject>, List<Int>> {
        return repository.loadInitialMuseumObjects(batchSize)
    }
}