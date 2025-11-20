import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.project.buildlogic.configureKotlinAndroid

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
                //configureFlavors(this)
            }

            extensions.configure<LibraryExtension> {
                libraryVariants.configureEach {
                    sourceSets {
                        getByName(name) {
                            kotlin.srcDir("build/generated/ksp/${name}/kotlin")
                        }
                    }
                }
            }
        }
    }
}