package metron

import LoginSuccess
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import korlibs.datastructure.*
import korlibs.io.lang.*
import korlibs.io.net.http.*
import korlibs.io.serialization.json.*
import kotlinx.uuid.*
import metron.model.*
import oauthClientId
import org.jetbrains.exposed.sql.transactions.*
import java.util.concurrent.*

val LoginTokens = ConcurrentHashMap<UUID, PlayerConnection>()

var PlayerConnection.clientUrl by Extra.Property { "undefined" }

fun Route.handleCallback() {
    get("callback") {
        val callback = OAuthCallback(call.parameters.toMap().mapValues { it.value[0] })
        val player = LoginTokens[UUID(callback.state)]!!
        val userprofile = callback.toUserProfile("${player.clientUrl}/callback")
        val user = transaction { User.new {
            email = userprofile.email
            username = userprofile.name
            locale = userprofile.locale
        } }
        player.send(LoginSuccess(user.username, user.id.value))
        call.respond("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
          <meta charset="utf-8">
          <title>이제 이 화면을 닫아도 됩니다</title>
        </head>
        <body>
            <h1>이제 이 화면을 닫아도 됩니다</h1>
        </body>
        </html>
    """.trimIndent())
    }
}

class OAuthCallback(map: Map<String, String>) {
    val state: String by map
    val code: String by map
    val scope: List<String> = map["scope"]!!.toString().stripBrackets().split(" ")
    val authuser: String by map
    val prompt: String by map
    val hello: String by map

    private fun String.stripBrackets() = substr(1, length - 2)

    internal val client by lazy { createHttpClient() }
    private lateinit var accessToken: String
}

suspend fun OAuthCallback.getAccessToken(callbackUrl: String) = client.request(
    Http.Method.POST, "https://oauth2.googleapis.com/token",
    HttpBodyContentFormUrlEncoded(
        "code" to code,
        "client_id" to oauthClientId,
        "client_secret" to run {
            println(SystemProperties.getAll().map { it.key }.joinToString("\n"))
            SystemProperties["OAUTH_CLIENT_SECRET"]!!
        },
        "redirect_uri" to callbackUrl,
        "grant_type" to "authorization_code"
    )
).readAllString().run(Json::parseFast).fastCastTo<Map<String, String>>()["access_token"]!!

class UserProfile(map: Map<String, String>) {
    val id by map
    val email by map
    val verified_email by map
    val name by map
    val given_name by map
    val family_name by map
    val picture by map
    val locale by map
}

suspend fun OAuthCallback.toUserProfile(callbackUrl: String) =
    getAccessToken(callbackUrl).let { accessToken ->
        client.request(
            Http.Method.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json",
            Http.Headers("Authorization" to "Bearer $accessToken")
        ).readAllString().run(Json::parseFast).fastCastTo<Map<String, String>>().run(::UserProfile)
    }

