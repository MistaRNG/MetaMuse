package com.example.metamuse.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MuseumObjectDto(
    @SerialName("objectID") val objectID: Int = -1,
    @SerialName("isHighlight") val isHighlight: Boolean = false,
    @SerialName("accessionNumber") val accessionNumber: String = "",
    @SerialName("accessionYear") val accessionYear: String = "",
    @SerialName("isPublicDomain") val isPublicDomain: Boolean = false,
    @SerialName("primaryImage") val primaryImage: String = "",
    @SerialName("primaryImageSmall") val primaryImageSmall: String = "",
    @SerialName("additionalImages") val additionalImages: List<String> = emptyList(),
    @SerialName("constituents") val constituents: List<ConstituentDto>? = null,
    @SerialName("department") val department: String = "",
    @SerialName("objectName") val objectName: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("culture") val culture: String = "",
    @SerialName("period") val period: String = "",
    @SerialName("dynasty") val dynasty: String = "",
    @SerialName("reign") val reign: String = "",
    @SerialName("portfolio") val portfolio: String = "",
    @SerialName("artistRole") val artistRole: String = "",
    @SerialName("artistPrefix") val artistPrefix: String = "",
    @SerialName("artistDisplayName") val artistDisplayName: String = "",
    @SerialName("artistDisplayBio") val artistDisplayBio: String = "",
    @SerialName("artistSuffix") val artistSuffix: String = "",
    @SerialName("artistAlphaSort") val artistAlphaSort: String = "",
    @SerialName("artistNationality") val artistNationality: String = "",
    @SerialName("artistBeginDate") val artistBeginDate: String = "",
    @SerialName("artistEndDate") val artistEndDate: String = "",
    @SerialName("artistGender") val artistGender: String = "",
    @SerialName("artistWikidata_URL") val artistWikidataUrl: String = "",
    @SerialName("artistULAN_URL") val artistUlanUrl: String = "",
    @SerialName("objectDate") val objectDate: String = "",
    @SerialName("objectBeginDate") val objectBeginDate: Int = 0,
    @SerialName("objectEndDate") val objectEndDate: Int = 0,
    @SerialName("medium") val medium: String = "",
    @SerialName("dimensions") val dimensions: String = "",
    @SerialName("measurements") val measurements: List<MeasurementDto>? = null,
    @SerialName("creditLine") val creditLine: String = "",
    @SerialName("geographyType") val geographyType: String = "",
    @SerialName("city") val city: String = "",
    @SerialName("state") val state: String = "",
    @SerialName("county") val county: String = "",
    @SerialName("country") val country: String = "",
    @SerialName("region") val region: String = "",
    @SerialName("subregion") val subregion: String = "",
    @SerialName("locale") val locale: String = "",
    @SerialName("locus") val locus: String = "",
    @SerialName("excavation") val excavation: String = "",
    @SerialName("river") val river: String = "",
    @SerialName("classification") val classification: String = "",
    @SerialName("rightsAndReproduction") val rightsAndReproduction: String = "",
    @SerialName("linkResource") val linkResource: String = "",
    @SerialName("metadataDate") val metadataDate: String = "",
    @SerialName("repository") val repository: String = "",
    @SerialName("objectURL") val objectURL: String = "",
    @SerialName("tags") val tags: List<TagDto>? = null,
    @SerialName("objectWikidata_URL") val objectWikidataUrl: String = "",
    @SerialName("isTimelineWork") val isTimelineWork: Boolean = false,
    @SerialName("GalleryNumber") val galleryNumber: String = ""
)

@Serializable
data class ConstituentDto(
    @SerialName("constituentID") val constituentID: Int = 0,
    @SerialName("role") val role: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("constituentULAN_URL") val constituentUlanUrl: String = "",
    @SerialName("constituentWikidata_URL") val constituentWikidataUrl: String = "",
    @SerialName("gender") val gender: String = ""
)

@Serializable
data class MeasurementDto(
    @SerialName("elementName") val elementName: String = "",
    @SerialName("elementDescription") val elementDescription: String? = null,
    @SerialName("elementMeasurements") val elementMeasurements: ElementMeasurementsDto = ElementMeasurementsDto()
)

@Serializable
data class ElementMeasurementsDto(
    @SerialName("Height") val height: Double = 0.0,
    @SerialName("Width") val width: Double = 0.0
)

@Serializable
data class TagDto(
    @SerialName("term") val term: String = "",
    @SerialName("AAT_URL") val AAT_URL: String?,
    @SerialName("Wikidata_URL") val wikidataUrl: String = ""
)
