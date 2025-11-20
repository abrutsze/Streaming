package com.project.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        val serialization = libs.findLibrary("kotlin.serialization").get()

        val compose = libs.findBundle("compose").get()
        val composeDebugTestManifest = libs.findLibrary("compose.debug.test.manifest").get()
        val coil = libs.findLibrary("coil.compose").get()
        val coilSvg = libs.findLibrary("coil.svg").get()

        dependencies {
            add("implementation", compose)
            add("implementation", coil)
            add("implementation", coilSvg)
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("debugImplementation", composeDebugTestManifest)
            add("implementation", serialization)

        }
    }
}
