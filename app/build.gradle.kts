plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.example.applicationsop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.applicationsop"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.99"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            exclude("META-INF/DEPENDENCIES")
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            exclude("win32-x86-64/attach_hotspot_windows.dll")
            exclude("win32-x86/attach_hotspot_windows.dll")
            exclude("META-INF/licenses/ASM")
            exclude("META-INF/LICENSE-notice.md")
            merges += "META-INF/LICENSE.md"

        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.storage)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.runtime.livedata)

    // Dependensi Ktor untuk koneksi client di Android
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ktor.server.test.host)

    // Depedencies untuk Image Loading
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")

//    dep refresh
    implementation(libs.swiperefreshlayout)
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation(libs.kotlin.test)
    implementation(libs.kotlinx.coroutines.debug)
    implementation(libs.byte.buddy.agent)

    // Icon
    implementation(libs.androidx.material.icons.extended)

    // Ktor Client dependencies
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

}