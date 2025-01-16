package xyz.teamgravity.serializationcustomserializerdemo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDescriptionDto(
    @SerialName("value") val value: String?
)
