@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Streaming"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(
    ":core:network:api",
    ":core:network:impl",
    ":core:dispatchers:api",
    ":core:dispatchers:impl",
    ":core:datastore:api",
    ":core:datastore:impl",
    ":core:database:api",
    ":core:database:impl",
    ":core:ui",
    ":core:resources",
)

include(
    ":common:response",
    ":common:utils",
    ":common:mvi",
    ":common:ui-models",
)

include(
    ":feature:app_splash",
    ":feature:streaming"
)

include(":navigation")
include(":screens")


