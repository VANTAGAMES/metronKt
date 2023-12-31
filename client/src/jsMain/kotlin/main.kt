import korlibs.io.file.std.*
import korlibs.io.lang.*
import kotlinx.browser.*
import metron.*

suspend fun main() {
    val clientProps = resourcesVfs["client.properties"].readProperties()
    currentUrl = clientProps["server"]!!
    version = clientProps["version"]!!
    redirector = { window.location.replace(it) }
    LoginTokenGetter = {
        document.cookie.apply { println(this) }.split("; ").associate {
            it.substringBefore("=") to it.substringAfter("=")
        }["LoginToken"]!! }
    LoginTokenSetter = { document.cookie = "LoginToken=$it" }
    clientWebsite = { window.location.href }
    startMain()
}
