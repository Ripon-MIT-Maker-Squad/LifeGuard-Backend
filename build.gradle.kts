
plugins {
    id("application")
}

group ="com.riponmakers.poolguard"
version ="0.0.5"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    implementation("io.helidon.openapi:helidon-openapi:2.6.0")
    implementation("io.helidon.integrations.openapi-ui:helidon-integrations-openapi-ui:3.2.2")
    implementation("io.helidon:helidon-dependencies:3.2.2")
    implementation("io.helidon.webserver:helidon-webserver:3.2.2")
    implementation("io.helidon.media.jsonp:helidon-media-jsonp-common:2.0.0-M3")
    implementation("io.helidon.config:helidon-config-yaml:3.2.2")
    implementation("io.helidon.health:helidon-health:3.2.2")
    implementation("io.helidon.health:helidon-health-checks:3.2.2")
    implementation("io.helidon.metrics:helidon-metrics:3.2.2")

    implementation("org.postgresql:postgresql:42.6.0")

    testImplementation("org.testcontainers:postgresql:1.18.3")
    testImplementation("org.testcontainers:testcontainers:1.18.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.helidon.webclient:helidon-webclient:3.2.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.riponmakers.poolguardian.Main")
}