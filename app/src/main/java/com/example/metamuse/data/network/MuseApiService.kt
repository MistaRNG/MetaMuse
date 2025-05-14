package com.example.metamuse.data.network

import com.example.metamuse.data.model.MuseumResponse
import com.example.metamuse.data.model.dto.MuseumObjectDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MuseApiService {

    @GET("objects")
    suspend fun getMuseumIDs(): MuseumResponse

    @GET("objects/{id}")
    suspend fun getMuseumObject(@retrofit2.http.Path("id") id: Int): MuseumObjectDto

    @GET("search")
    suspend fun searchMuseumObjects(
        @Query("q") query: String
    ): MuseumResponse
}
