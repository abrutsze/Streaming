plugins {
    alias(libs.plugins.project.android.library)
}

android {
    namespace = "com.android.network.response"
}

dependencies {
    // Serialization
    implementation(libs.kotlin.serialization)
}
