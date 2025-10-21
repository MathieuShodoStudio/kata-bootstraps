plugins {
    kotlin("jvm") version "2.2+"
}

group = "com.kata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.cucumber:cucumber-java8:7+")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7+")
    testImplementation("org.junit.platform:junit-platform-suite:1+")
}

tasks.test {
    useJUnitPlatform()
}
