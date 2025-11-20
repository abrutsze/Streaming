plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.library.compose)
    alias(libs.plugins.project.android.feature)
}

android {
    namespace = "com.android.feature.custom_splash"
}

dependencies {

    implementation(projects.core.datastore.api)

}
