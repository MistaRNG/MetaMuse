package com.example.metamuse.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MuseumObject(
    @SerialName("objectID") val objectID: Int,
    @SerialName("title") val title: String,
    @SerialName("primaryImage") val primaryImage: String,
    @SerialName("additionalImages") val additionalImages: List<String> = emptyList(),
    @SerialName("artistDisplayName") val artistDisplayName: String,
    @SerialName("artistDisplayBio") val artistDisplayBio: String,
    @SerialName("objectDate") val objectDate: String,
    @SerialName("medium") val medium: String,
    @SerialName("dimensions") val dimensions: String,
    @SerialName("objectURL") val objectURL: String
)
