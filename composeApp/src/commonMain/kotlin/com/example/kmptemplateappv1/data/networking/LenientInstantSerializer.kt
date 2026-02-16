@file:OptIn(ExperimentalTime::class)
@file:Suppress("DEPRECATION", "OPT_IN_USAGE", "OPT_IN_OVERRIDE")

package com.example.kmptemplateappv1.data.networking

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant as DateTimeInstant

/**
 * Custom serializer for kotlinx.datetime.Instant that handles datetime strings
 * without timezone info (like '0001-01-01T00:00:00').
 */
@OptIn(ExperimentalTime::class)
object LenientInstantSerializer : KSerializer<DateTimeInstant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    @OptIn(ExperimentalTime::class)
    override fun deserialize(decoder: Decoder): DateTimeInstant {
        val string = decoder.decodeString()
        return try {
            DateTimeInstant.parse(string)
        } catch (_: Exception) {
            try {
                LocalDateTime.parse(string).toInstant(TimeZone.UTC)
            } catch (_: Exception) {
                DateTimeInstant.fromEpochMilliseconds(0)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun serialize(encoder: Encoder, value: DateTimeInstant) {
        encoder.encodeString(value.toString())
    }
}

/**
 * Pre-configured SerializersModule with LenientInstantSerializer for use across platforms.
 */
@OptIn(ExperimentalTime::class)
val LenientInstantSerializerModule = SerializersModule {
    contextual(DateTimeInstant::class, LenientInstantSerializer)
}
