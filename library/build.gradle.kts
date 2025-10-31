import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    id("com.vanniktech.maven.publish") version "0.34.0"
    signing
}

val artifactBaseId = rootProject.name.lowercase()
val artifactVersion = project.version.toString().substringBeforeLast('-')

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    useGpgCmd()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2-1")
    listOf(
        "com.google.code.gson:gson:2.13.2",
        "it.unimi.dsi:fastutil:8.5.18"
    ).forEach {
        compileOnly(it)
        testImplementation(it)
    }
}

tasks {
    jar {
        archiveBaseName = rootProject.name
    }
    test {
        useJUnitPlatform()
    }
}

mavenPublishing  {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.toxicity188", artifactBaseId, artifactVersion)
    configure(JavaLibrary(
        javadocJar = JavadocJar.None(),
        sourcesJar = true,
    ))
    pom {
        name = artifactBaseId
        description = "12-limb Minecraft armor model."
        inceptionYear = "2025"
        url = "https://github.com/toxicity188/ArmorModel/"
        licenses {
            license {
                name = "MIT License"
                url = "https://mit-license.org/"
            }
        }
        developers {
            developer {
                id = "toxicity188"
                name = "toxicity188"
                url = "https://github.com/toxicity188/"
            }
        }
        scm {
            url = "https://github.com/toxicity188/ArmorModel/"
            connection = "scm:git:git://github.com/toxicity188/ArmorModel.git"
            developerConnection = "scm:git:ssh://git@github.com/toxicity188/ArmorModel.git"
        }
    }
}