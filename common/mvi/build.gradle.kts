plugins {
    alias(libs.plugins.project.android.library)
}

android {
    namespace = "com.android.mvi"

    dependencies {
        implementation(projects.core.network.api)
        implementation(projects.core.ui)
        implementation(projects.screens)

    }
}
