plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.library.compose)
}

android {
    namespace = "com.android.navigation"
}

dependencies {
    implementation(projects.feature.appSplash)
    implementation(projects.feature.streaming)
    implementation(projects.screens)
    implementation(libs.androidx.navigation)
}
