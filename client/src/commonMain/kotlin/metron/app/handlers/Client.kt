package metron.app.handlers

import LoginSuccess
import LoginStart
import Packet
import RedirectRequest
import korlibs.event.*
import korlibs.io.net.*
import korlibs.io.net.ws.*
import korlibs.korge.view.*
import korlibs.memory.*
import korlibs.render.*
import kotlinx.uuid.*
import metron.*
import oauthClientId
import serialize
import util.*

suspend fun enableClient() = client {
    val loginToken = runCatching { LoginTokenGetter?.invoke() }
        .getOrNull()?.toUUIDOrNull()?: UUID.generateUUID()
    LoginTokenSetter?.invoke(loginToken.toString())
    onEvent(LoginSuccess) {
        println("LoginSuccess")
        state = Packet.State.PLAY
        LoginTokenSetter?.invoke(it.loginToken.toString())
    }
    send(LoginStart("", version, loginToken, currentUrl))
//    if (!Platform.isJs) {
//        startLogin(loginToken.toString())
//    }
    onEvent(RedirectRequest) {
        launchNow { startLogin(loginToken.toString()) }
    }
}

class PlayerConnection(val socket: WebSocketClient) : BaseEventListener() {
    var state: Packet.State = Packet.State.LOGIN
    fun close() = socket.close()
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

private suspend fun startLogin(loginToken: String) = redirector(URL(
    "https://accounts.google.com/o/oauth2/v2/auth?".plus(QueryString.encode(
        "scope" to "email",
        "access_type" to "offline",
        "include_granted_scopes" to "true",
        "response_type" to "code",
        "state" to loginToken,
        "redirect_uri" to "$currentUrl/callback",
        "client_id" to oauthClientId
    ))).apply { println(this.toUrlString())}.toString())
