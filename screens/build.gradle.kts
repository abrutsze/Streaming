plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization")
}

android {
    namespace = "com.android.screens"
}

dependencies {
    // Serialization
    implementation(libs.kotlin.serialization)

}
