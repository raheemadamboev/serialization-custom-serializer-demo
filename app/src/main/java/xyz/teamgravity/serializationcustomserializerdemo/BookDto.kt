package xyz.teamgravity.serializationcustomserializerdemo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(BookDtoSerializer::class)
data class BookDto(
    @SerialName("description") val description: String?
)
