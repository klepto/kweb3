plugins {
    id("java")
}

group = "dev.klepto.kweb3"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(libs.google.guava)
    implementation(libs.google.gson)
    implementation(libs.headlong)
    implementation(libs.unreflect)
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