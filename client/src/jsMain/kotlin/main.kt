import korlibs.io.file.std.*
import korlibs.io.lang.*
import kotlinx.browser.*
import metron.*

suspend fun main() {
    val clientProps = resourcesVfs["client.properties"].readProperties()
    currentUrl = clientProps["server"]!!
    version = clientProps["version"]!!
    redirector = { window.location.replace(it) }
    LoginTokenGetter = { document.cookie.split(";").map {
        it.substringBefore("=") to it.substringAfter("=")
    }.toMap()["LoginToken"]!! }
    LoginTokenSetter = { document.cookie = "LoginToken=$it" }
    startMain()
}
