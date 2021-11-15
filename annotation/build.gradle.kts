plugins {
    kotlin("jvm")
    java
}

dependencies {
    implementation(kotlin("stdlib"))
    
    api(project(":runtime"))
}