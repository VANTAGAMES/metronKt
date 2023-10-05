package metron.app.handlers

import LoginSuccess
import LoginStart
import Packet
import korlibs.event.*
import korlibs.io.net.*
import korlibs.io.net.ws.*
import korlibs.render.*
import kotlinx.uuid.*
import metron.*
import oauthClientId
import serialize
import util.*

suspend fun enableClient() = client {
    val loginToken = UUID.generateUUID()
    send(LoginStart("Bruce0203", version, loginToken, currentUrl))
    startLogin(loginToken.toString())
    onEvent(LoginSuccess) {
        println("LoginSuccess")
        state = Packet.State.PLAY
    }
}

class PlayerConnection(val socket: WebSocketClient) : BaseEventListener() {
    var state: Packet.State = Packet.State.LOGIN
}

private fun PlayerConnection.send(packet: Packet) = launchNow { socket.send(packet.serialize()) }

suspend fun client(onConnect: suspend PlayerConnection.() -> Unit) =
    WebSocketClient(webSocketUrl.apply(::println)).let { socket ->
        val player = PlayerConnection(socket).apply { onConnect() }
        launchNow {
            socket.onBinaryMessage {
                runCatching {
                    val packet = Packet.deserialize(player.state, it, Packet.Bound.CLIENT)
                    player.dispatch(packet as BEvent)
                }.exceptionOrNull()?.printStackTrace()
            }
        }
        player
    }



private val webSocketUrl = URL(currentUrl).let {
    "${it.scheme!!.httpToWs()}://${it.fullUrlWithoutScheme}"
}
private fun String.httpToWs() = if (startsWith("https")) "wss" else "ws"

private suspend fun startLogin(loginToken: String) = scene.views.gameWindow.browse(URL(
    "https://accounts.google.com/o/oauth2/v2/auth?" + QueryString.encode(
        "scope" to "email",
        "access_type" to "offline",
        "include_granted_scopes" to "true",
        "response_type" to "code",
        "state" to loginToken,
        "redirect_uri" to "$currentUrl/callback",
        "client_id" to oauthClientId
    )).apply { println(this.toUrlString())})
