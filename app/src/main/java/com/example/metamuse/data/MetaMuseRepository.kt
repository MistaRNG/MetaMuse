package com.example.metamuse.data

import com.example.metamuse.domain.model.MuseumObject
import com.example.metamuse.data.mapper.toDomain
import com.example.metamuse.data.network.MuseApiService

interface MetaMuseRepository {
    suspend fun getMuseumIDs(): List<Int>
    suspend fun getMuseumObject(id: Int): MuseumObject
    suspend fun searchMuseumObjectIDs(query: String): List<Int>
}

internal class NetworkMetaMuseRepository(
    private val museApiService: MuseApiService
) : MetaMuseRepository {

    override suspend fun getMuseumIDs(): List<Int> {
        return museApiService.getMuseumIDs().objectIDs ?: emptyList()
    }

    override suspend fun getMuseumObject(id: Int): MuseumObject {
        return try {
            val dto = museApiService.getMuseumObject(id)
            dto.toDomain()
        } catch (e: Exception) {
            println("❌ Failed to fetch DTO: ${e.message}")
            throw e
        }
    }

    override suspend fun searchMuseumObjectIDs(query: String): List<Int> {
        return museApiService.searchMuseumObjects(
            query = query
        ).objectIDs ?: emptyList()
    }
}
