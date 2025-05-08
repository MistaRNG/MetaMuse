package com.example.metamuse.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MuseumResponse(
    @SerialName("total") val total: Int,
    @SerialName("objectIDs") val objectIDs: List<Int>? = null
)
