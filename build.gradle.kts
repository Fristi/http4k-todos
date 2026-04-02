plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "1.9.0"
    application
}



group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))


    // OpenTelemetry SDK
    implementation("io.opentelemetry:opentelemetry-sdk:1.36.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.36.0")

    // http4k
    implementation(platform("org.http4k:http4k-bom:5.+"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-ops-opentelemetry")
    implementation("org.http4k:http4k-contract")           // OpenAPI / routing DSL
    implementation("org.http4k:http4k-format-kotlinx-serialization") // JSON lens
    implementation("org.http4k:http4k-format-jackson")

    // Configuration
    implementation("com.uchuhimo:konf-core:1.1.2")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:0.50.+")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.+")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.+")
    implementation("com.h2database:h2:2.2.+")              // swap for Postgres in prod


    // OpenTelemetry SDK
    implementation("io.opentelemetry:opentelemetry-sdk:1.36.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.36.0")

    // Koin
    implementation("io.insert-koin:koin-core:3.5.+")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.+")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}