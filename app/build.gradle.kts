@file:Suppress("UnstableApiUsage","NewerVersionAvailable","GradleDependency","DEPRECATION")
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// == LOAD LOCAL.PROPERTIES == //
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    namespace = "com.crescenzi.buck"
    compileSdk = 36

    // == USE THIS VERSION OF NDK FOR 16 KB ALIGNMENT (REQUIRED FOR PLAY STORE) == //
    ndkVersion = "29.0.13846066"
    defaultConfig {
        applicationId = "com.crescenzi.buck"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations += listOf("en", "it")
        buildConfigField("String", "COHERE_API_KEY", "\"${localProps.getProperty("COHERE_API_KEY", "")}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes{
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.10.1")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okio:okio:3.7.0")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
}
