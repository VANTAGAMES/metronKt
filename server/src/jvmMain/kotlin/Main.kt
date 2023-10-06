import korlibs.datastructure.*
import kotlinx.uuid.*
import metron.*
import metron.model.*
import metron.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

var PlayerConnection.loginToken by Extra.Property { UUID() }
var PlayerConnection.clientWebsite by Extra.Property { "undefined" }

fun main() {
    connect("app")
    Packet //instantiate packet definitions
    server {
        onEvent(SocketClosedEvent) {
            state = Packet.State.CLOSED
        }
        onEvent(PingRequest) {
            launchNow { send(PingResponse()) }
        }
        onEvent(LoginStart) {
            clientWebsite = it.clientWebsite
            clientUrl = it.currentUrl
            loginToken = it.loginToken
            launchNow {
                if (LoginTokens.contains(loginToken)) {
                    send(transaction {
                        User[loginToken].run { LoginSuccess(username, id.value, loginToken) }
                    })
                } else {
                    transaction { LoginTokens[loginToken] = this@server }
                    send(RedirectRequest())
                }
            }
        }
        onEvent(LoginStart) {
            println("LoginStart")
        }
    }
}

fun connect(db: String) = Database.connect("jdbc:h2:file:./$db", driver = "org.h2.Driver")
