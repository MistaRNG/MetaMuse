package com.example.metamuse.data

import com.example.metamuse.data.model.MuseumObject
import com.example.metamuse.data.network.MuseApiService

interface MetaMuseRepository {
    suspend fun getMuseumIDs(): List<Int>
    suspend fun getMuseumObject(id: Int): MuseumObject
    suspend fun searchMuseumObjectIDs(query: String): List<Int>
}

class NetworkMetaMuseRepository(
    private val museApiService: MuseApiService
) : MetaMuseRepository {

    override suspend fun getMuseumIDs(): List<Int> {
        return museApiService.getMuseumIDs().objectIDs ?: emptyList()
    }

    override suspend fun getMuseumObject(id: Int): MuseumObject {
        return museApiService.getMuseumObject(id)
    }

    override suspend fun searchMuseumObjectIDs(query: String): List<Int> {
        return museApiService.searchMuseumObjects(
            query = query
        ).objectIDs ?: emptyList()
    }
}
