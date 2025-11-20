plugins {
    alias(libs.plugins.project.android.library)
}

android {
    namespace = "com.android.database.api"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.room.runtime)

}
