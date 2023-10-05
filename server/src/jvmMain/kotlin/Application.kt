import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

fun main() {
    connect("app")
    server {
        onEvent(PingRequest) { send(PingResponse()) }
        onEvent(LoginStart) {
            clientUrl = it.currentUrl
            transaction { LoginTokens[it.loginToken] = this@server }
        }
        onEvent(LoginStart) {
            println("LoginStart")
        }
    }
}

fun connect(db: String) = Database.connect("jdbc:h2:file:./$db", driver = "org.h2.Driver")
