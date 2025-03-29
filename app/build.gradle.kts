// Plugins block specifies which plugins are applied to this module
plugins {
    alias(libs.plugins.android.application) // Applies the Android application plugin
    alias(libs.plugins.kotlin.android)     // Applies the Kotlin Android plugin
//    id("com.android.application") //had to comment out this line because build was failing with this was already done at line two error
    id("com.google.gms.google-services")
}

// Android configuration block
android {
    namespace = "com.example.trucking" // The unique namespace for the application
    compileSdk = 35 // Specifies the Android SDK version to compile the application against

    defaultConfig {
        applicationId = "com.example.trucking" // Unique identifier for the app on the Play Store
        minSdk = 24 // Minimum supported Android version (API Level 24 = Android 7.0 Nougat)
        targetSdk = 35 // Targeted Android SDK version for compatibility with latest features
        versionCode = 1 // Internal version code for app updates
        versionName = "1.0" // Human-readable version name for the app

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner for instrumentation tests
    }

    // Build types specify configurations for release and debug builds
    buildTypes {
        release {
            isMinifyEnabled = false // Disables code shrinking, obfuscation, and optimization for this build type
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default ProGuard rules for optimization
                "proguard-rules.pro" // Custom ProGuard rules
            )
        }
    }

    // Specifies Java compatibility versions for the app
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // The source code is compatible with Java 11
        targetCompatibility = JavaVersion.VERSION_11 // The compiled output will target Java 11
    }

    // Kotlin-specific configuration
    kotlinOptions {
        jvmTarget = "11" // Sets the Kotlin JVM target version to 11 for compatibility with Java 11
    }
}

// Dependencies block declares the external libraries the app relies on
dependencies {

    // Core libraries for Android development
    implementation(libs.androidx.core.ktx)          // Core extensions for AndroidX
    implementation(libs.androidx.appcompat)         // AppCompat library for backward compatibility
    implementation(libs.material)                  // Material Design components
    implementation(libs.androidx.activity)         // Activity library for AndroidX
    implementation(libs.androidx.constraintlayout) // ConstraintLayout for flexible layouts

    // Google Maps and Firebase integration
    implementation(libs.play.services.maps)        // Google Maps SDK for Android
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)    // Firebase Firestore library for real-time database integration
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Testing dependencies
    testImplementation(libs.junit)                 // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit) // AndroidX JUnit extensions for Android testing
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}
