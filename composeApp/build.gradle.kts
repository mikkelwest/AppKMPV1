import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.generator)

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // OpenAPI generated Kotlin (Ktor) client will be added to commonMain


    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
           // implementation(libs.lifecycle.viewmodel)
           // implementation(libs.navigation.compose)


            api(libs.datastore.preferences)
            api(libs.datastore)

            implementation(libs.bundles.ktor)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.kmptemplateappv1"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.kmptemplateappv1"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Configure OpenAPI Generator to produce a Kotlin Multiplatform client using Ktor
openApiGenerate {
    // Use the kotlin generator with multiplatform library
    generatorName.set("kotlin")
    inputSpec.set(rootProject.file("openapi/swagger.json").absolutePath)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)


    // The Kotlin generator uses 'packageName' to set the base package
    packageName.set("com.example.kmptemplateappv1.openapi")
    apiPackage.set("com.example.kmptemplateappv1.openapi.api")
    modelPackage.set("com.example.kmptemplateappv1.openapi.model")

    // Use multiplatform library - generates Ktor-based code compatible with all KMP targets
    configOptions.set(mapOf(
        "library" to "multiplatform",
        "serializationLibrary" to "kotlinx_serialization",
        "dateLibrary" to "kotlinx-datetime",
        "useCoroutines" to "true",
        "omitGradleWrapper" to "true"
    ))
}

// Add generated sources to commonMain (multiplatform library generates KMP-compatible code)
kotlin.sourceSets.getByName("commonMain") {
    kotlin.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin"))
}

// Fix duplicate @Serializable annotations and incorrect Instant type in generated code
val fixOpenApiGeneratedCode by tasks.registering {
    dependsOn("openApiGenerate")

    val generatedDir = layout.buildDirectory.dir("generated/openapi/src/main/kotlin")
    inputs.dir(generatedDir)
    outputs.dir(generatedDir)

    doLast {
        generatedDir.get().asFile.walkTopDown().filter { it.extension == "kt" }.forEach { file ->
            var content = file.readText()
            var modified = false

            // Fix duplicate @Serializable annotations
            if (content.contains("@Serializable@Serializable")) {
                content = content.replace("@Serializable@Serializable", "@Serializable")
                modified = true
                println("Fixed duplicate @Serializable in: ${file.name}")
            }

            // Fix incorrect kotlin.time.Instant -> kotlinx.datetime.Instant
            if (content.contains("kotlin.time.Instant")) {
                content = content.replace("kotlin.time.Instant", "kotlinx.datetime.Instant")
                modified = true
                println("Fixed kotlin.time.Instant -> kotlinx.datetime.Instant in: ${file.name}")
            }

            // Add @Contextual annotation and @OptIn for Instant fields (serialization support)
            if (content.contains("kotlinx.datetime.Instant")) {
                // Add OptIn file annotation if not present (for kotlin.time.ExperimentalTime)
                if (!content.contains("@file:OptIn")) {
                    content = content.replace(
                        "@file:Suppress(",
                        "@file:OptIn(kotlin.time.ExperimentalTime::class)\n@file:Suppress("
                    )
                }
                // Add Contextual import if not present
                if (!content.contains("import kotlinx.serialization.Contextual")) {
                    content = content.replace(
                        "import kotlinx.serialization.*",
                        "import kotlinx.serialization.*\nimport kotlinx.serialization.Contextual"
                    )
                }
                // Add @Contextual before Instant type declarations
                content = content.replace(
                    Regex("""val (\w+): kotlinx\.datetime\.Instant\?"""),
                    "@Contextual val $1: kotlinx.datetime.Instant?"
                )
                modified = true
                println("Added @Contextual for Instant in: ${file.name}")
            }

            if (modified) {
                file.writeText(content)
            }
        }
    }
}

// Ensure generation runs before Kotlin compilation tasks
// Use afterEvaluate to safely wire up dependencies without early resolution
afterEvaluate {
    tasks.findByName("compileCommonMainKotlinMetadata")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileKotlinAndroid")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileDebugKotlinAndroid")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileReleaseKotlinAndroid")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileKotlinIosX64")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileKotlinIosArm64")?.dependsOn(fixOpenApiGeneratedCode)
    tasks.findByName("compileKotlinIosSimulatorArm64")?.dependsOn(fixOpenApiGeneratedCode)
}

dependencies {
    debugImplementation(compose.uiTooling)
}
