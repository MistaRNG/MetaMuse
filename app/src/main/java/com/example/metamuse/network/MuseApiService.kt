package com.example.metamuse.network

import com.example.metamuse.model.MuseumObject
import com.example.metamuse.model.MuseumResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MuseApiService {

    @GET("objects")
    suspend fun getMuseumIDs(): MuseumResponse

    @GET("objects/{id}")
    suspend fun getMuseumObject(@retrofit2.http.Path("id") id: Int): MuseumObject

    @GET("search")
    suspend fun searchMuseumObjects(
        @Query("q") query: String
    ): MuseumResponse
}
