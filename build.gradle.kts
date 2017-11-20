import nu.studer.gradle.credentials.CredentialsPlugin
import nu.studer.gradle.credentials.domain.CredentialsContainer
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    kotlin("jvm") version "1.1.60"
    id("org.jetbrains.intellij") version("0.2.17")
    id("nu.studer.credentials") version("1.0.3")
}

repositories {
    jcenter()
}

dependencies {
    compileOnly(kotlin("stdlib-jre8"))
    compileOnly(kotlin("reflect"))
}

val publishPlugin: PublishTask by tasks
val credentials: CredentialsContainer by extra
val javaVersion = "1.8"

intellij {
    version = "2016.1"
    updateSinceUntilBuild = false
}

java {
    setSourceCompatibility(javaVersion)
    setTargetCompatibility(javaVersion)
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = javaVersion
}

publishPlugin {
    username(credentials["JETBRAINS_USERNAME"])
    password(credentials["JETBRAINS_PASSWORD"])
}

group = "net.olegg"
version = "0.0.4-SNAPSHOT"

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
operator fun CredentialsContainer.get(key: String) = this.getProperty(key)
