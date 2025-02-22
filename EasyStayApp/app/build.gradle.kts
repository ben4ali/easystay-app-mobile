plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.easycorp.easystayapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.easycorp.easystayapp"
        minSdk = 34
        targetSdk = 34
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

    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.testng)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.android.material:material:1.9.0")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.tbuonomo:dotsindicator:5.1.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}