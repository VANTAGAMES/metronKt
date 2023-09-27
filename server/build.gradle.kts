import korlibs.korge.gradle.*

apply<KorgeGradlePlugin>()
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.korge)
}

korge {
    targetJvm()
    targetJs()
    targetDesktop()
    //targetDesktopCross()
    serializationJson()
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
        val commonMain by getting {
            dependencies {
//                api(libs.kotlinx.uuid)
            }
        }
    }
}
