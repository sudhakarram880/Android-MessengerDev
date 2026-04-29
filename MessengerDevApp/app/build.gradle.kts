import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}
val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
android {
    namespace = "com.sk.messanger"
    compileSdk = 36
    /*compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }*/

    defaultConfig {
        applicationId = "com.sk.messanger"
        minSdk = 24
        targetSdk = 36
        versionCode = versionMajor * 10000 + versionMinor * 100  + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"

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
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //firebase realtime db and auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.gson)
    implementation(libs.androidx.lifecycle.viewmodel)
    ksp(libs.hilt.android.compiler)

// Kotlin Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)


}