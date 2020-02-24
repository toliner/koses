package dev.toliner.koses.test.jdk

import dev.toliner.koses.jdk.LocalDateTimeSerializer
import dev.toliner.koses.jdk.UUIDSerializer
import dev.toliner.koses.jdk.ZonedDateTimeSerializer
import io.kotest.property.arbitrary.Arb
import io.kotest.property.arbitrary.arb
import io.kotest.property.arbitrary.localDateTime
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@UseExperimental(UnstableDefault::class, ImplicitReflectionSerializer::class)
class JdkSerializersTest {
    @Test
    fun `test UUIDSerializer`() {
        @Serializable
        data class A(@Serializable(with = UUIDSerializer::class) val a: UUID)
        repeat(1000) {
            val uuid = UUID.randomUUID()
            val data = A(uuid)
            testWithJson(data)
        }
    }

    @Test
    fun `test DateTime`() {
        @Serializable
        data class A(
            @Serializable(with = LocalDateTimeSerializer::class)
            val a: LocalDateTime,
            @Serializable(with = ZonedDateTimeSerializer::class)
            val b: ZonedDateTime
        )

        fun randomZone(): ZoneId = ZoneId.of(ZoneId.SHORT_IDS.toList().random().second)
        val arb = Arb.localDateTime(maxYear = 2200)
        arb.edgecases().forEach {
            val zone = randomZone()
            val local = it
            val zoned = ZonedDateTime.of(it, zone).withFixedOffsetZone()
            testWithJson(A(local, zoned))
        }
        arb { rs ->
            arb.samples(rs).take(1000).forEach {
                val zone = randomZone()
                val local = it.value
                val zoned = ZonedDateTime.of(it.value, zone).withFixedOffsetZone()
                testWithJson(A(local, zoned))
            }
        }
    }

    private inline fun <reified T : Any> testWithJson(value: T) {
        val serializer = T::class.serializer()
        val json = Json.stringify(serializer, value)
        val restored = Json.parse(serializer, json)
        assertEquals(value, restored)
    }
}