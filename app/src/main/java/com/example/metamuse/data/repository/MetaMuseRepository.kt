package com.example.metamuse.data.repository

import com.example.metamuse.domain.model.MuseumObject

interface MetaMuseRepository {
    suspend fun loadInitialMuseumObjects(batchSize: Int): Pair<List<MuseumObject>, List<Int>>
    suspend fun loadNextMuseumObjects(
        allIds: List<Int>,
        offset: Int,
        batchSize: Int
    ): List<MuseumObject>

    suspend fun searchMuseumObjects(query: String, limit: Int = 50): List<MuseumObject>
    suspend fun getMuseumObject(id: Int): MuseumObject
}
