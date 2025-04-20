plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.project" // Your app's namespace
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

    dependencies {
        // If using version catalog, the libs variable should be defined in libs.versions.toml
        dependencies {
            implementation(libs.appcompat)
            implementation(libs.material)
            implementation(libs.constraintlayout)
            implementation(libs.lifecycle.livedata.ktx)
            implementation ("com.google.android.material:material:1.11.0")
            implementation ("com.squareup.picasso:picasso:2.71828")
            implementation ("androidx.annotation:annotation:1.7.1")
            implementation(libs.lifecycle.viewmodel.ktx)
            implementation(libs.navigation.fragment)
            implementation(libs.navigation.ui)
            implementation(libs.zxing)
            testImplementation(libs.junit)
            androidTestImplementation(libs.ext.junit)
            androidTestImplementation(libs.espresso.core)
    }

}

