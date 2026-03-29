plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "de.beckerasano.currencycalculator"
    compileSdk = 36

    defaultConfig {
        applicationId = "de.beckerasano.currencycalculator"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlin {
        jvmToolchain(11) // Or 17, or 21, as long as they match
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
// Ktor Client Core
    implementation("io.ktor:ktor-client-core:3.4.2")
    // Ktor Engine (CIO is recommended for Android/Kotlin)
    implementation("io.ktor:ktor-client-cio:3.4.2")

    // Content Negotiation (to handle JSON)
    implementation("io.ktor:ktor-client-content-negotiation:3.4.2")

    // Kotlinx Serialization for Ktor
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.2")

    // (Optional) Logging if you want to see network requests in Logcat
    implementation("io.ktor:ktor-client-logging:3.4.2")
implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}