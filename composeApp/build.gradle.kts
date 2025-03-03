import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(enforcedPlatform("com.google.firebase:firebase-bom:33.9.0"))
            implementation("com.google.firebase:firebase-auth-ktx")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
            // Firebase
            implementation("com.google.firebase:firebase-auth-ktx:22.1.0")
            implementation("androidx.compose.ui:ui:1.5.0")
            implementation("androidx.compose.material3:material3:1.1.0")
            implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
            // Google Sign-In
            implementation("com.google.android.gms:play-services-auth:20.7.0")
            // CameraX
            implementation("androidx.camera:camera-core:1.3.0")
            implementation("androidx.camera:camera-camera2:1.3.0")
            implementation("androidx.camera:camera-lifecycle:1.3.0")
            implementation("androidx.camera:camera-view:1.3.0")
            implementation("androidx.navigation:navigation-compose:2.7.6")
            implementation("com.google.mlkit:face-detection:16.1.5")
        }
    }
}

android {
    namespace = "com.healthmonitor.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.healthmonitor.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    signingConfigs {
        create("customDebug") {
            storeFile = File("/Users/premdas/keystore/health/health.keystore")
            storePassword = "healthmonitor"
            keyAlias = "monitor"
            keyPassword = "healthmonitor"
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("customDebug")
        }
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}

