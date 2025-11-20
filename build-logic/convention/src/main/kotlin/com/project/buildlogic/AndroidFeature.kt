package com.project.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

internal fun Project.configureFeature(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {

        val test = libs.findBundle("test").get()
        val androidTest = libs.findBundle("androidTest").get()
        val androidX = libs.findBundle("androidx").get()
        val appcompat = libs.findLibrary("androidx-appcompat").get()
        val kspCompiler = libs.findLibrary("koin.ksp.compiler").get()


        dependencies {
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:dispatchers:api"))
            add("implementation", project(":common:mvi"))
            add("implementation", project(":common:utils"))
            add("implementation", project(":common:response"))
            add("implementation", project(":core:network:api"))
            add("implementation", project(":screens"))
            add("implementation", project(":core:resources"))
            add("implementation", project(":common:ui-models"))

            // AndroidX
            add("implementation", androidX)
            add("implementation", appcompat)
            add("ksp", kspCompiler)

            // Tests
            add("androidTestImplementation", androidTest)
            add("testImplementation", test)
        }
    }
}
