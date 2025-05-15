package com.example.metamuse.di

import com.example.metamuse.data.network.MuseApiService
import com.example.metamuse.data.repository.MetaMuseRepository
import com.example.metamuse.data.repository.impl.MetaMuseRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMuseApiService(): MuseApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MuseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMetaMuseRepository(apiService: MuseApiService): MetaMuseRepository {
        return MetaMuseRepositoryImpl(apiService)
    }
}
