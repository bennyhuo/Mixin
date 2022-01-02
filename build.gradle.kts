buildscript {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.10.1")
    }
}

val GROUP: String by extra
val VERSION_NAME: String by extra

subprojects {
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
    
    if (!name.startsWith("sample")) {
        group = GROUP
        version = VERSION_NAME

        apply(plugin = "com.vanniktech.maven.publish")
    }
}
