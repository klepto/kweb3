group = "dev.klepto.kweb3"
version = "0.0.1"

plugins {
    id("io.freefair.lombok") version "8.6" apply false
}

subprojects {
    apply {
        plugin("java-library")
        plugin("io.freefair.lombok")
    }

    repositories {
        mavenCentral()
    }
}