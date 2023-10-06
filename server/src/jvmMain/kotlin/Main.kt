import korlibs.datastructure.*
import kotlinx.uuid.*
import metron.*
import metron.util.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

var PlayerConnection.loginToken by Extra.Property { UUID() }

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
            clientUrl = it.currentUrl
            loginToken = it.loginToken
            transaction { LoginTokens[loginToken] = this@server }
        }
        onEvent(LoginStart) {
            println("LoginStart")
        }
    }
}

fun connect(db: String) = Database.connect("jdbc:h2:file:./$db", driver = "org.h2.Driver")
