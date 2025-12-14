plugins {
    alias(libs.plugins.android.application)
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "com.elective.school_management_system"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.elective.school_management_system"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- AR Dependencies (DOWNGRADED TO 0.10.2 TO FIX ERRORS) ---
    implementation("com.google.ar:core:1.46.0")

    // IMPORTANT: Use version 0.10.2 to match your Java code!
    implementation("io.github.sceneview:arsceneview:0.10.2")
}