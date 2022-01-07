import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":annotations"))

    implementation("androidx.room:room-compiler-processing:2.4.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.0-1.0.1")
    
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.6")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.6")

    testImplementation("com.bennyhuo.kotlin:kotlin-compile-testing-extensions:0.2")

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-Xjvm-default=all",
            "-Xuse-experimental=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview",
            "-Xuse-experimental=com.google.devtools.ksp.KspExperimental",
            "-Xopt-in=androidx.room.compiler.processing.ExperimentalProcessingApi"
        )
    }
}