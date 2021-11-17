plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
    id("com.google.devtools.ksp") version "1.5.31-1.0.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":annotation"))

    ksp(project(":compiler"))
    kapt(project(":compiler"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

sourceSets.main {
    java.srcDir("build/generated/ksp/main/kotlin")
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}