import korlibs.korge.gradle.*

apply<KorgeGradlePlugin>()
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.korge)
    alias(libs.plugins.kotlinx.serialization)
}

version = ""

korge {
    targetJvm()
    targetJs()
    targetDesktop()
    //targetDesktopCross()
}

tasks.all {
    if (name == "prepareKotlinNativeBootstrap") onlyIf { false }
    if (name.contains("mingwX64", ignoreCase = true)) {
        onlyIf { it.name == "compileKotlinMingwX64" }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(libs.kotlinx.uuid)
                api(libs.kotlinx.uuid.exposed)
                api(libs.h2database)
                api(project(":shared"))
                api(libs.kotlinx.serialization.protobuf)
                api(libs.exposed.jdbc)
                api(libs.exposed.core)
                api(libs.exposed.dao)


                api(libs.ktor.server.netty)
                api(libs.ktor.server.core)
                api(libs.ktor.server.websockets)
                api(libs.logback)
                api(libs.ktor.server.cors)
                api(libs.ktor.server.auth)
                api(libs.ktor.server.sessions)
                api(libs.ktor.server.content.negotation)
            }
        }
    }
}
