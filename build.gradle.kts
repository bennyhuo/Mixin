plugins {
    kotlin("jvm") version "1.5.31"
    java
}

allprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}
