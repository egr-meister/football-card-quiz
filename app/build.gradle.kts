import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

// ---- Read API config from local.properties (never hardcode the token) ----
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) {
        FileInputStream(f).use { load(it) }
    }
}

fun prop(key: String, default: String): String {
    // Priority: environment variable (CI) -> local.properties -> default
    return System.getenv(key) ?: localProps.getProperty(key) ?: default
}

val footballApiToken: String = prop("FOOTBALL_DATA_API_TOKEN", "your_api_token_here")
val footballApiBaseUrl: String = prop("FOOTBALL_API_BASE_URL", "https://api.football-data.org/v4")

// ---- Read signing config from environment (CI) or keystore.properties (local) ----
val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties().apply {
    if (keystorePropsFile.exists()) {
        FileInputStream(keystorePropsFile).use { load(it) }
    }
}

fun signingProp(envKey: String, fileKey: String): String? =
    System.getenv(envKey) ?: keystoreProps.getProperty(fileKey)

android {
    namespace = "com.footballcardquiz.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.footballcardquiz.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        // Portrait quiz app; no test runner needed for release stability.
        vectorDrawables { useSupportLibrary = true }

        // Expose API config via BuildConfig (token is never committed to source).
        buildConfigField("String", "FOOTBALL_DATA_API_TOKEN", "\"$footballApiToken\"")
        buildConfigField("String", "FOOTBALL_API_BASE_URL", "\"$footballApiBaseUrl\"")
    }

    signingConfigs {
        create("release") {
            val storePath = System.getenv("ANDROID_KEYSTORE_PATH")
                ?: keystoreProps.getProperty("storeFile")
            val storePw = signingProp("ANDROID_KEYSTORE_PASSWORD", "storePassword")
            val alias = signingProp("ANDROID_KEY_ALIAS", "keyAlias")
            val keyPw = signingProp("ANDROID_KEY_PASSWORD", "keyPassword")

            if (storePath != null && storePw != null && alias != null && keyPw != null) {
                storeFile = file(storePath)
                storePassword = storePw
                keyAlias = alias
                keyPassword = keyPw
                storeType = "PKCS12"
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            // Verified stable first with minify disabled; R8/shrink enabled after.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Use the release signing config so the very first release build is signed.
            val relConfig = signingConfigs.getByName("release")
            if (relConfig.storeFile != null) {
                signingConfig = relConfig
            }
        }
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        // Ensure 16 KB page-size alignment for any bundled native libs (uncompressed).
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.8.1")

    // Splash screen (custom, no default Android splash).
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Local storage
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // Networking (secondary Match Schedule feature only)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
}
