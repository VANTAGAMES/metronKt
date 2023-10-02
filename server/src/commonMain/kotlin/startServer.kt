import korlibs.event.*
import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.io.net.http.*

fun startServer() = server {
    onEvent(PingRequestPacket) { send(PingResponsePacket()) }
    onEvent(LoginStartPacket) {
        println("A")
        state = Packet.State.PLAY
    }
}

class PlayerConnection(
    val socket: HttpServer.WsRequest
) : BaseEventListener() {
    var  state: Packet.State = Packet.State.HANDSHAKE
}

fun server(onAccept: PlayerConnection.() -> Unit) = runBlockingNoJs<Unit> {
    Packet //instantiate packet definitions
    createHttpServer().websocketHandler { socket ->
        val player = PlayerConnection(socket).apply(onAccept)
        socket.onBinaryMessage { payload ->
            val packet = Packet.deserialize(player.state, payload, Packet.Bound.SERVER)
            player.dispatch(packet as BEvent)
        }
    }.listen(port = 8082, host = "127.0.0.1").apply {
        httpHandler { call ->
            if (call.absoluteURI != "/shutdown") return@httpHandler
            if (call.method != Http.Method.POST) return@httpHandler
            val key = SystemProperties["shutdown.key"]
            if (key == call.readRawBody(12800).decodeToString()) {
                println("Shutdown the server")
                close()
            }
            call.close()
        }.also {
            println("starting the server")
        }
    }
}

fun PlayerConnection.send(packet: Packet) = socket.send(packet.serialize())
