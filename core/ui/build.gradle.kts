plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.library.compose)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
android {
    namespace = "com.android.ui"
}

dependencies {
    implementation(projects.core.resources)
    implementation(projects.common.uiModels)
}
