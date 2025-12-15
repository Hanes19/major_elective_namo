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

    // GPS/Location Dependency (Already Present)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // ADDED: Google Maps SDK Dependency
    implementation("com.google.android.gms:play-services-maps:18.2.0")
}