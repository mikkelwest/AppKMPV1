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

// Configure OpenAPI Generator to produce a Kotlin client (for Android/JVM only due to OkHttp dependency)
openApiGenerate {
    // Use the kotlin generator
    generatorName.set("kotlin")
    inputSpec.set(rootProject.file("openapi/swagger.json").absolutePath)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)

    // The Kotlin generator uses 'packageName' to set the base package
    packageName.set("com.example.kmptemplateappv1.openapi")
    apiPackage.set("com.example.kmptemplateappv1.openapi.api")
    modelPackage.set("com.example.kmptemplateappv1.openapi.model")

    // Use kotlinx_serialization and configure for JVM/Android usage
    configOptions.set(mapOf(
        "serializationLibrary" to "kotlinx_serialization",
        "dateLibrary" to "string",
        "useCoroutines" to "true",
        "library" to "jvm-okhttp4"
    ))
}

// Add generated sources to androidMain only (since it uses OkHttp which is JVM/Android-only)
kotlin.sourceSets.getByName("androidMain") {
    kotlin.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin"))
}

// Ensure generation runs before Kotlin compilation tasks
// Use afterEvaluate to safely wire up dependencies without early resolution
afterEvaluate {
    tasks.findByName("compileCommonMainKotlinMetadata")?.dependsOn("openApiGenerate")
    tasks.findByName("compileKotlinAndroid")?.dependsOn("openApiGenerate")
    tasks.findByName("compileDebugKotlinAndroid")?.dependsOn("openApiGenerate")
    tasks.findByName("compileReleaseKotlinAndroid")?.dependsOn("openApiGenerate")
    tasks.findByName("compileKotlinIosX64")?.dependsOn("openApiGenerate")
    tasks.findByName("compileKotlinIosArm64")?.dependsOn("openApiGenerate")
    tasks.findByName("compileKotlinIosSimulatorArm64")?.dependsOn("openApiGenerate")
}

dependencies {
    debugImplementation(compose.uiTooling)
}
