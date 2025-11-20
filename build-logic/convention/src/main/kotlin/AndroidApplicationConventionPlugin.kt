import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.project.buildlogic.configureFlavors
import com.project.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                buildFeatures {
                    buildConfig = true
                }
                defaultConfig.targetSdk = 35
                configureFlavors(this)
            }

            extensions.configure<BaseAppModuleExtension> {
                applicationVariants.all {
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
