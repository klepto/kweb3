plugins {
    id("java-library")
}

group = "dev.klepto.kweb3"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.unreflect)
    api(libs.google.guava)

    implementation(libs.jetbrains.annotations)
    implementation(libs.google.gson)
    implementation(libs.headlong)
    implementation(libs.java.websocket)
    implementation(libs.bundles.slf4j)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}