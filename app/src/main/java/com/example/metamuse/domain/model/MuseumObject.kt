package com.example.metamuse.domain.model

data class MuseumObject(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val additionalImages: List<String>,
    val artist: String,
    val artistBio: String,
    val date: String,
    val medium: String,
    val dimensions: String,
    val url: String
)
