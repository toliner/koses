package dev.toliner.koses.jdk

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * When serializing, only ISO-8601 based DateTime are supported.
 * [ZonedDateTime]'s region-based zone IDs are ignored when serializing.
 * Before serializing, [ZonedDateTime.withFixedOffsetZone] is recommended.
 */
@Suppress("unused")
@Serializer(forClass = ZonedDateTime::class)
object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("ZonedDateTimeSerializer")

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME)
    }

    override fun serialize(encoder: Encoder, obj: ZonedDateTime) {
        encoder.encodeString(obj.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }
}