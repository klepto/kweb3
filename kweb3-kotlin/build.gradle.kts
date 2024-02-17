plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.kweb3Core)
    implementation(projects.kweb3Multicall)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.coroutines)
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}