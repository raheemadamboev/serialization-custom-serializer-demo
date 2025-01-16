package xyz.teamgravity.serializationcustomserializerdemo

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object BookDtoSerializer : KSerializer<BookDto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        serialName = BookDto::class.simpleName!!
    ) {
        element<String?>("description")
    }

    override fun deserialize(decoder: Decoder): BookDto {
        return decoder.decodeStructure(descriptor) {
            var description: String? = null

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> {
                        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("This serializer only works with JSON.")
                        val element = jsonDecoder.decodeJsonElement()

                        description = when {
                            element is JsonObject -> decoder.json.decodeFromJsonElement(
                                element = element,
                                deserializer = BookDescriptionDto.serializer()
                            ).value

                            element is JsonPrimitive && element.isString -> element.content
                            else -> null
                        }
                    }

                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw SerializationException("Invalid index!")
                }
            }

            return@decodeStructure BookDto(
                description = description
            )
        }
    }

    override fun serialize(encoder: Encoder, value: BookDto) {
        encoder.encodeStructure(descriptor) {
            value.description?.let { encodeStringElement(descriptor, 0, it) }
        }
    }
}