package com.example.metamuse.data.mapper

import com.example.metamuse.data.model.dto.MuseumObjectDto
import com.example.metamuse.domain.model.MuseumObject

fun MuseumObjectDto.toDomain(): MuseumObject {
    return MuseumObject(
        id = objectID,
        title = title,
        imageUrl = primaryImage,
        additionalImages = additionalImages,
        artist = artistDisplayName,
        artistBio = artistDisplayBio,
        date = objectDate,
        medium = medium,
        dimensions = dimensions,
        url = objectURL
    )
}
