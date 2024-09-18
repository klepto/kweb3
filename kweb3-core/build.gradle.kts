dependencies {
    api(libs.unreflect)
    api(libs.google.guava)

    implementation(libs.jetbrains.annotations)
    implementation(libs.google.gson)
    implementation(libs.headlong)
    implementation(libs.java.websocket)
    implementation(libs.bundles.slf4j)

    implementation(platform(libs.unirest.bom))
    implementation(libs.unirest.core)
    implementation(libs.unirest.gson)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}