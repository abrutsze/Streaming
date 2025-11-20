plugins {
    alias(libs.plugins.project.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.android.database.impl"
}

dependencies {
    implementation(projects.core.database.api)
    implementation(libs.bundles.koin)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

}
