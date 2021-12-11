plugins {
    kotlin("jvm") version "1.6.0"
    java
}

allprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}
