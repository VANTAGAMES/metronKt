import korlibs.datastructure.*
import korlibs.datastructure.iterators.*
import korlibs.io.stream.*
import kotlinx.serialization.*
import kotlinx.serialization.protobuf.*
import kotlin.reflect.*

interface Packet {
    val id: Int
    val state: State
    val kType: KType
    enum class State { LOGIN, PLAY }
    enum class Bound { CLIENT, SERVER }
    companion object Registry {
        private val clientBound = listOf(
            PingResponse,
            GameJoinPacket,
            LoginSuccess,
        ).toPacketMap()
        private val serverBound = listOf(
            LoginStart,
            PingRequest,
        ).toPacketMap()
        private fun List<Packet>.toPacketMap() = intMapOf<IntMap<Packet>>().also { map ->
            fastForEach { map.getOrPut(it.state.ordinal) { intMapOf() }[it.id] = it }
        }
        operator fun get(id: Int, state: State, bound: Bound) = when(bound) {
            Bound.CLIENT -> clientBound
            Bound.SERVER -> serverBound
        }[state.ordinal]?.get(id)?: throw AssertionError("${
            clientBound.toMap().map { it.key to it.value.toMap() } +
            serverBound.toMap().map { it.key to it.value.toMap() }
        }\n$bound packet id $id in $state not found")
        @OptIn(ExperimentalSerializationApi::class)
        fun deserialize(state: State, payload: ByteArray, bound: Bound): Any {
            val stream = payload.openFastStream()
            val packetID = stream.readS8()
            val format = ProtoBuf
            val type = Packet[packetID, state, bound].kType
            val data = stream.readBytesExact(stream.available)
            return format.decodeFromByteArray(format.serializersModule.serializer(type), data)!!
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Packet.serialize(): ByteArray {
    val format = ProtoBuf
    return byteArrayOf(id.toByte()) +
        format.encodeToByteArray(format.serializersModule.serializer(kType), this)
}

interface ClientBound : Packet
interface ServerBound : Packet
data class PacketImp(
    override val id: Int,
    override val state: Packet.State,
    override val kType: KType
) : Packet
inline fun <reified T> packet(id: Int, state: Packet.State) =
    PacketImp(id = id, state = state, kType = typeOf<T>())
inline fun <reified T> Play(id: Int) = packet<T>(id, Packet.State.PLAY)
inline fun <reified T> Login(id: Int) = packet<T>(id, Packet.State.LOGIN)
