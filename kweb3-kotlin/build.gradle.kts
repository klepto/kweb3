plugins {
    alias(libs.plugins.kotlin.jvm) version "1.9.23"
}

dependencies {
    implementation(projects.kweb3Core)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.coroutines)
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}