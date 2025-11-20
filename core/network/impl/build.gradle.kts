plugins {
    alias(libs.plugins.project.android.library)
}

android {
    namespace = "com.android.network.impl"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(projects.core.network.api)
    implementation(projects.common.response)
    implementation(projects.core.datastore.api)
    implementation(libs.kotlin.serialization)
    implementation(libs.bundles.ktor)
}
