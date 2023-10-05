import korlibs.datastructure.*
import korlibs.event.*
import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.io.net.http.*

fun PlayerConnection.send(packet: Packet) = socket.send(packet.serialize())

class PlayerConnection(
    val socket: HttpServer.WsRequest,
) : EventListener by BaseEventListener(), Extra by Extra.Mixin() {
    var state: Packet.State = Packet.State.LOGIN
}

fun server(onAccept: PlayerConnection.() -> Unit) = runBlockingNoJs<Unit> {
    println("Starting the server...")
    Packet //instantiate packet definitions
    createHttpServer().websocketHandler { socket ->
        val player = PlayerConnection(socket).apply(onAccept)
        socket.onBinaryMessage { payload ->
            val packet = Packet.deserialize(player.state, payload, Packet.Bound.SERVER)
            player.dispatch(packet as BEvent)
        }
    }.listen(port = 8080, host = "127.0.0.1").apply {
        httpHandler { call ->
            when (call.absoluteURI.substringBefore("?")) {
                "/shutdown" -> call.handleShutdown()
                "/callback" -> call.handleCallback()
            }
            call.close()
        }.also {
            println("Server Started!")
        }
    }
}

private suspend fun HttpServer.Request.handleShutdown() {
    if (method != Http.Method.POST) return
    val key = SystemProperties["SHUTDOWN_KEY"]
    if (key == readRawBody(12800).decodeToString()) {
        println("Shutdown the server")
        close()
    }
}

