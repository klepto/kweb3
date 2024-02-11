plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.kweb3Core)
    implementation(libs.kotlin.reflect)
}

kotlin {
    jvmToolchain(17)
}