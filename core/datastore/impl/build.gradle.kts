plugins {
    alias(libs.plugins.project.android.library)
    kotlin("plugin.serialization")
}
android {
    namespace = "com.android.datastore.impl"
}
dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.common.utils)
    implementation(libs.kotlin.serialization)
}
