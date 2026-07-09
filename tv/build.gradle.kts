apply(plugin = "com.android.application")
apply(plugin = "com.google.devtools.ksp")

configure<com.android.build.api.dsl.ApplicationExtension> {
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
}

dependencies {
    // Leanback Library — el estándar de Android TV
    add("implementation", "androidx.leanback:leanback:1.2.0")
    // Glide para cargar imágenes en las cards
    add("implementation", "com.github.bumptech.glide:glide:4.16.0")
    
    // Room dependencies for local database
    val roomVersion = "2.6.1"
    add("implementation", "androidx.room:room-runtime:$roomVersion")
    add("implementation", "androidx.room:room-ktx:$roomVersion")
    add("ksp", "androidx.room:room-compiler:$roomVersion")

    // Media3 + ExoPlayer for TV audio playback
    val media3Version = "1.4.1"
    add("implementation", "androidx.media3:media3-exoplayer:$media3Version")
    add("implementation", "androidx.media3:media3-ui:$media3Version")
    // LeanbackPlayerAdapter conecta ExoPlayer con la UI de Leanback en Media3
    add("implementation", "androidx.media3:media3-ui-leanback:$media3Version")

    // Fragment & ViewModel support
    add("implementation", "androidx.fragment:fragment-ktx:1.8.2")
    // ViewModel + Coroutines
    add("implementation", "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
}
