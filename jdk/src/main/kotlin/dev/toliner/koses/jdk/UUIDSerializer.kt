package dev.toliner.koses.jdk


import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.util.*

@Suppress("unused")
@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("UUIDSerializer")

    override fun serialize(encoder: Encoder, obj: UUID) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}