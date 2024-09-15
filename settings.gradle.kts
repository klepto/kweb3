plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "kweb3"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include("kweb3-core")
include("kweb3-kotlin")
include("kweb3-contracts")