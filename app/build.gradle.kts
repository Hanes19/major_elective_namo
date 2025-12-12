plugins {
    alias(libs.plugins.android.application)
}

// Required because settings.gradle is set to PREFER_PROJECT
repositories {
    google()
    mavenCentral()
    /*flatDir {
        // Points to the libs folder inside unityLibrary
        dirs("../unityLibrary/libs")
    }*/
}

android {
    namespace = "com.elective.school_management_system"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.elective.school_management_system"
        minSdk = 25  // Must match unityLibrary
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

    // Updated to Java 17 to match UnityLibrary
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Links the Unity Library Module
   // implementation(project(":unityLibrary"))

    //implementation(files("../unityLibrary/libs/unity-classes.jar"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}