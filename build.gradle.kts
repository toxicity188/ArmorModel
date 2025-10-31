plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")

    group = "kr.toxicity.library.armormodel"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.13.4"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")

        testCompileOnly("org.projectlombok:lombok:1.18.42")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    }

    tasks {
        test {
            useJUnitPlatform()
        }
        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }
    }
}

