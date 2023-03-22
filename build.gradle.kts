plugins {
    id("org.jetbrains.kotlin.jvm").version("1.8.10")
    id("org.jetbrains.kotlin.plugin.serialization").version("1.8.10")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("net.mbonnin.bare-graphql:bare-graphql:0.0.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}