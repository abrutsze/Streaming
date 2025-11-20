plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.project.android.library.compose)
    alias(libs.plugins.project.android.feature)
}

android {
    namespace = "com.android.feature.streaming"
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.database.api)
    implementation(libs.coil.compose)
    implementation(libs.haishinkit.core)
    implementation(libs.haishinkit.rtmp)
}

