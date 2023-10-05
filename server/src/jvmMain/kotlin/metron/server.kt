package metron

import Packet
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import korlibs.datastructure.*
import korlibs.event.*
import korlibs.io.lang.SystemProperties
import serialize

fun server(
    client: suspend PlayerConnection.() -> Unit
) = embeddedServer(Netty, port = SystemProperties["PORT"]?.toInt()?: 8080) {
    install(WebSockets)
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 }
    }
    routing {
        webSocket {
            val player = PlayerConnection { send(it) }
            client(player)
            for (frame in incoming) {
                frame as? Frame.Binary ?: continue
                val receivedText = frame.readBytes()
                val packet = Packet.deserialize(player.state, receivedText, Packet.Bound.SERVER)
                player.dispatch(packet as BEvent)
            }
        }
        handleCallback()
        post("shutdown") {
            if (call.receiveText() == SystemProperties["SHUTDOWN_KEY"]) {
                println("Shutdown the server")
                ShutDownUrl("shutdown") { 0 }.doShutdown(call)
            }
        }
    }
}.start(wait = true)

suspend fun PlayerConnection.send(packet: Packet) = send(packet.serialize())

class PlayerConnection(
    val writeChannel: suspend (ByteArray) -> Unit
) : EventListener by BaseEventListener(), Extra by Extra.Mixin() {
    var state: Packet.State = Packet.State.LOGIN
    suspend fun send(byteArray: ByteArray) = writeChannel(byteArray)
}
