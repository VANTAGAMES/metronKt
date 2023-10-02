import korlibs.event.*
import korlibs.time.*
import kotlinx.atomicfu.*
import kotlinx.serialization.*

private val clientIdCounter = atomic(0)
private val serverIdCounter = atomic(0)

fun generateServerID() = serverIdCounter.getAndAdd(1)
fun generateClientID() = clientIdCounter.getAndAdd(1)

@Serializable
data class LoginStartPacket(
    val version: String
) : Event(), TEvent<LoginStartPacket>, Packet by this {
    companion object : EventType<LoginStartPacket>,
        Packet by Handshake<LoginStartPacket>(generateServerID()), ServerBound
    override val type: EventType<LoginStartPacket> get() = LoginStartPacket
}

@Serializable
data class LoginResponsePacket(
    val username: String,
) : Event(), TEvent<LoginResponsePacket>, Packet by this {
    companion object : EventType<LoginResponsePacket>,
        Packet by Handshake<LoginResponsePacket>(generateClientID()), ClientBound
    override val type: EventType<LoginResponsePacket> get() = LoginResponsePacket
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
data class PingRequestPacket @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val time: Long = DateTime.now().unixMillisLong,
) : Event(), TEvent<PingRequestPacket>, Packet by this {
    companion object : EventType<PingRequestPacket>,
        Packet by Play<PingRequestPacket>(generateServerID()), ServerBound
    override val type: EventType<PingRequestPacket> get() = PingRequestPacket
}

@Serializable
data class PingResponsePacket(
    val time: Long = DateTime.now().unixMillisLong,
) : Event(), TEvent<PingResponsePacket>, Packet by this {
    companion object : EventType<PingResponsePacket>,
        Packet by Play<PingResponsePacket>(generateClientID()), ClientBound
    override val type: EventType<PingResponsePacket> get() = PingResponsePacket
}
