package serializer

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.uuid.*

object PacketUUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder) =
        UUID(ByteArray(16) { decoder.decodeByte() })

    override fun serialize(encoder: Encoder, value: UUID) {
        value.encodeToByteArray().forEach { encoder.encodeByte(it) }
    }
}
