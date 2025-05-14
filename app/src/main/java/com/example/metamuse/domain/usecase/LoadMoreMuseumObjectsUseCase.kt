package com.example.metamuse.domain.usecase

import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import javax.inject.Inject

class LoadMoreMuseumObjectsUseCase @Inject constructor(
    private val repository: MetaMuseRepository
) {
    suspend operator fun invoke(allIDs: List<Int>, loadedItemCount: Int, batchSize: Int): List<MuseumObject> {
        return repository.loadNextMuseumObjects(allIDs, loadedItemCount, batchSize)
    }
}