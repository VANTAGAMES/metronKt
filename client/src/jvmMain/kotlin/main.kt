import metron.*
import java.util.*

class Main
suspend fun main() {
    val clientProps = Properties().apply {
        load(Main::class.java.getResourceAsStream("client.properties"))
    }
    currentUrl = clientProps["server"]!!.toString()
    version = clientProps["version"]!!.toString()
    startMain()
}
