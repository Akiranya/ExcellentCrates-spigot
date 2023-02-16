/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("net.kyori.indra.git") version "2.1.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        content {
            includeGroup("io.papermc.paper")
            includeGroup("net.md-5")
        }
    }
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.MilkBowl")
            includeGroup("com.github.decentsoftware-eu")
        }
    }
    maven("https://repo.citizensnpcs.co/#/") {
        content {
            includeGroup("net.citizensnpcs")
        }
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        content {
            includeGroup("me.clip")
        }
    }
    maven("https://repo.codemc.io/repository/maven-public/") {
        content {
            includeGroup("com.gmail.filoghost.holographicdisplays")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("su.nexmedia:NexEngine:2.2.8")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.2.5")
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.0")
    compileOnly("net.citizensnpcs:citizens-main:2.0.29-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.10.10")
}

group = "su.nightexpress.excellentcrates"
version = "4.1.6".decorateVersion()
description = "ExcellentCrates"

fun lastCommitHash(): String = indraGit.commit()?.name?.substring(0, 7) ?: error("Could not determine commit hash")
fun String.decorateVersion(): String = if (endsWith("-SNAPSHOT")) "$this-${lastCommitHash()}" else this

bukkit {
    main = "su.nightexpress.excellentcrates.ExcellentCrates"
    name = "ExcellentCrates"
    version = "${project.version}"
    description = "Fully customizable crates with rewards. Enjoy."
    apiVersion = "1.17"
    authors = listOf("NightExpress")
    depend = listOf("NexEngine")
    softDepend = listOf(
        "Citizens",
        "HolographicDisplays",
        "DecentHolograms",
        "Vault",
        "PlaceholderAPI"
    )
}

tasks {
    jar {
        archiveFileName.set("ExcellentCrates-${project.version}.jar")
        archiveClassifier.set("")
        destinationDirectory.set(file("$rootDir"))
    }
    register("deployJar") {
        doLast {
            exec {
                commandLine("rsync", jar.get().archiveFile.get().asFile.absoluteFile, "dev:data/dev/jar")
            }
        }
    }
    register("deployJarFresh") {
        dependsOn(build)
        finalizedBy(named("deployJar"))
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
