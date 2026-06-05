plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "mx.utng.smarthealthmonitor.wear"

    compileSdk = 36

    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.wear"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose base
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.wear.compose.ui.tooling)

    // Wear Compose (ESTO ARREGLA foundation)
    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.material3)
    implementation(libs.androidx.wear.tooling.preview)

    // Material normal (opcional)
    implementation(libs.androidx.compose.material3)

    // Wear OS
    implementation(libs.play.services.wearable)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // Coroutines + Health base (si lo usas)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("com.google.guava:guava:33.0.0-android")
    implementation("androidx.health:health-services-client:1.0.0-beta01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")
    implementation("androidx.health:health-services-client:1.0.0-beta01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")
}