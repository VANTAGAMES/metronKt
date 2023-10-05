import korlibs.korge.gradle.*
import korlibs.korge.gradle.typedresources.*

apply<KorgeGradlePlugin>()
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.korge)
    alias(libs.plugins.kotlinx.serialization)
}

korge {
    targetJvm()
    targetJs()
    targetDesktop()
    //targetDesktopCross()
}

tasks.create<Delete>("disableKRes") {
    dependsOn(tasks.withType<GenerateTypedResourcesTask>())
    afterEvaluate { File(buildDir, "KR/KR.kt").delete() }
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
                api(libs.kotlinx.serialization.protobuf)
                api(libs.kotlinx.uuid)
            }
        }
    }
}
