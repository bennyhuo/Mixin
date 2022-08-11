import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("androidx.room:room-compiler-processing:2.4.2")
    compileOnly("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")

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