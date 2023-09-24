
plugins {
    id("application")
}

group = "com.riponmakers.lifeguard"
version = "0.0.9"

repositories {
    mavenCentral()
}

dependencies {
    /*
    * TEST CONTAINERS
    * */
    testImplementation("org.testcontainers:postgresql:1.18.3")
    testImplementation("org.testcontainers:testcontainers:1.18.3")

    /*
    * HELIDON MP
    * */
    implementation("io.helidon.microprofile.bundles:helidon-microprofile:3.2.2")
//    implementation("io.helidon.microprofile.metrics:helidon-microprofile-metrics:2.3.2")
//    implementation("io.helidon.microprofile.openapi:helidon-microprofile-openapi:2.3.2")
//    implementation("org.eclipse.microprofile.openapi:microprofile-openapi-spec:2.1")
//    testImplementation("io.helidon.microprofile.cdi:helidon-microprofile-cdi:2.3.2")

    /*
    * POSTGRESQL
    * */
    implementation("org.postgresql:postgresql:42.6.0")

    /*
    * JSON
    * */
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    /*
    * TESTING ENVIRONMENT
    * */
    testImplementation("org.testcontainers:postgresql:1.18.3")
    testImplementation("org.testcontainers:testcontainers:1.18.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.riponmakers.LifeGuardWebServer.Main")
}