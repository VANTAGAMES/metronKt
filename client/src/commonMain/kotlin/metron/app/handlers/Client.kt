package metron.app.handlers

import LoginResponsePacket
import LoginStartPacket
import Packet
import korlibs.event.*
import korlibs.io.net.*
import korlibs.io.net.ws.*
import metron.*
import serialize
import util.*

suspend fun enableClient() = client {
    send(LoginStartPacket(version))
    onEvent(LoginResponsePacket) {
        state = Packet.State.PLAY
        println("Logged in")
    }
}

class PlayerConnection(val socket: WebSocketClient) : BaseEventListener() {
    var state: Packet.State = Packet.State.HANDSHAKE
}

private fun PlayerConnection.send(packet: Packet) = launchNow { socket.send(packet.serialize()) }

suspend fun client(onConnect: PlayerConnection.() -> Unit) =
    WebSocketClient(webSocketUrl).let { socket ->
        val player = PlayerConnection(socket).apply(onConnect)
        socket.onBinaryMessage {
            val packet = Packet.deserialize(player.state, it, Packet.Bound.CLIENT)
            player.dispatch(packet as BEvent)
        }
    }



private val webSocketUrl = URL(currentUrl).let { "ws://${it.fullUrlWithoutScheme}" }

