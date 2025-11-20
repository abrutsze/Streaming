plugins {
    alias(libs.plugins.project.android.library)

}

android {
    namespace = "com.android.network.api"

}

dependencies {
    implementation(projects.common.response)
}
