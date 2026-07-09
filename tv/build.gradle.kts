plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Glide para cargar imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room dependencies for local database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Media3 + ExoPlayer
    val media3Version = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-datasource:$media3Version")

    // Compose BOM & Core
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)

    // TV Compose libraries
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0")

    // Navigation and Lifecycle support in Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
}
