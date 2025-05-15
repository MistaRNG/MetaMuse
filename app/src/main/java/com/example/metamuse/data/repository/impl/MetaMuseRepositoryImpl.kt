package com.example.metamuse.data.repository.impl

import com.example.metamuse.data.mapper.toDomain
import com.example.metamuse.data.network.MuseApiService
import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.domain.model.MuseumObject
import javax.inject.Inject

internal class MetaMuseRepositoryImpl @Inject constructor(
    private val museApiService: MuseApiService
) : MetaMuseRepository {

    override suspend fun loadInitialMuseumObjects(batchSize: Int): Pair<List<MuseumObject>, List<Int>> {
        val allIds = museApiService.getMuseumIDs().objectIDs ?: emptyList()
        val objects = getMuseumObjectsByIDs(allIds.take(batchSize))
        return Pair(objects, allIds)
    }

    override suspend fun loadNextMuseumObjects(allIds: List<Int>, offset: Int, batchSize: Int): List<MuseumObject> {
        val nextBatch = allIds.drop(offset).take(batchSize)
        return getMuseumObjectsByIDs(nextBatch)
    }

    override suspend fun searchMuseumObjects(query: String, limit: Int): List<MuseumObject> {
        return try {
            val ids = museApiService.searchMuseumObjects(query).objectIDs ?: emptyList()
            getMuseumObjectsByIDs(ids.take(limit))
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getMuseumObjectsByIDs(ids: List<Int>): List<MuseumObject> {
        return ids.mapNotNull { id ->
            try {
                museApiService.getMuseumObject(id).toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun getMuseumObject(id: Int): MuseumObject {
        return museApiService.getMuseumObject(id).toDomain()
    }
}