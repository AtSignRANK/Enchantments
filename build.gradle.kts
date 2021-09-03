plugins {
    kotlin("jvm") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.monun:kommand-api:2.6.5") // May be I will add command

    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16" // JDK 버전
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}