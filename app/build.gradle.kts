plugins {
    alias(libs.plugins.project.android.application)
    alias(libs.plugins.project.android.application.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.android.streaming"

    defaultConfig {
        applicationId = "com.android.streaming"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    val keyStoreFilePath_release: String by project

    signingConfigs {
        create("release") {
            storeFile = file(keyStoreFilePath_release)
            storePassword = providers.gradleProperty("keyStoreFilePassword_release").get()
            keyAlias = providers.gradleProperty("keyStoreKeyAlias_release").get()
            keyPassword = providers.gradleProperty("keyStoreKeyPassword_release").get()
        }
    }

    buildTypes {
        debug {

        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.network.api)
    implementation(projects.core.network.impl)
    implementation(projects.core.dispatchers.impl)
    implementation(projects.core.database.impl)
    implementation(projects.core.ui)
    implementation(projects.common.uiModels)
    implementation(projects.core.datastore.impl)
    implementation(projects.navigation)
    implementation(projects.screens)
    implementation(projects.feature.streaming)

    implementation(libs.bundles.androidx)

    // TEST
    testImplementation(libs.bundles.test)
    debugImplementation(libs.compose.debug.test.manifest)
}