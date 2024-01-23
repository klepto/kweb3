plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jetbrains.annotations)
    implementation(libs.guava)
    implementation(libs.headlong)
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