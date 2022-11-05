repositories {
    mavenCentral()
}
plugins {
    kotlin("jvm")
    java
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.bennyhuo.kotlin:annotations-module-support:1.7.10.1")
}