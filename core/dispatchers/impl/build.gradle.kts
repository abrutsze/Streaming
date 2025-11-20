plugins {
    alias(libs.plugins.project.android.library)
}
android {
    namespace = "com.android.dispatchers.impl"
}
dependencies {

    implementation(projects.core.dispatchers.api)
    implementation(libs.kotlinx.coroutines.core)
}