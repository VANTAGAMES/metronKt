import korlibs.event.*
import korlibs.time.*
import kotlinx.atomicfu.*
import kotlinx.serialization.*
import kotlinx.uuid.*
import serializer.*

private val clientIdCounter = atomic(0)
private val serverIdCounter = atomic(0)

fun generateServerID() = serverIdCounter.getAndAdd(1)
fun generateClientID() = clientIdCounter.getAndAdd(1)

@Serializable
data class LoginStart(
    val name: String,
    val version: String,
//    @Serializable(PacketUUIDSerializer::class)
    val loginToken: UUID,
    val currentUrl: String
) : Event(), TEvent<LoginStart>, Packet by this {
    companion object : EventType<LoginStart>,
        Packet by Login<LoginStart>(generateServerID()), ServerBound
    override val type: EventType<LoginStart> get() = LoginStart
}

@Serializable
data class LoginSuccess(
    val username: String,
//    @Serializable(PacketUUIDSerializer::class)
    val userUuid: UUID,
) : Event(), TEvent<LoginSuccess>, Packet by this {
    companion object : EventType<LoginSuccess>,
        Packet by Login<LoginSuccess>(generateClientID()), ClientBound
    override val type: EventType<LoginSuccess> get() = LoginSuccess
}

@Serializable
data class GameJoinPacket(
    val name: String,
) : Event(), TEvent<GameJoinPacket>, Packet by this {
    companion object : EventType<GameJoinPacket>,
        Packet by Play<GameJoinPacket>(generateClientID()), ClientBound
    override val type: EventType<GameJoinPacket> get() = GameJoinPacket
}

@Serializable
data class PingRequest @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val time: Long = DateTime.now().unixMillisLong,
) : Event(), TEvent<PingRequest>, Packet by this {
    companion object : EventType<PingRequest>,
        Packet by Play<PingRequest>(generateServerID()), ServerBound
    override val type: EventType<PingRequest> get() = PingRequest
}

@Serializable
data class PingResponse(
    val time: Long = DateTime.now().unixMillisLong,
) : Event(), TEvent<PingResponse>, Packet by this {
    companion object : EventType<PingResponse>,
        Packet by Play<PingResponse>(generateClientID()), ClientBound
    override val type: EventType<PingResponse> get() = PingResponse
}
