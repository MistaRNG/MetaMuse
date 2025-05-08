package com.example.metamuse.data

import com.example.metamuse.data.network.MuseApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val metaMuseRepository: MetaMuseRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://collectionapi.metmuseum.org/public/collection/v1/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MuseApiService by lazy {
        retrofit.create(MuseApiService::class.java)
    }

    override val metaMuseRepository: MetaMuseRepository by lazy {
        NetworkMetaMuseRepository(retrofitService)
    }
}