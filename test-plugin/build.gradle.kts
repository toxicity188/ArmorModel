plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.1"
    id("com.gradleup.shadow") version "9.3.1"
}

val testVersion = "1.21.11"

dependencies {
    paperweight.paperDevBundle("$testVersion-R0.1-SNAPSHOT")
    implementation(project(":library"))
}

tasks.runServer {
    version(testVersion)
    pluginJars(fileTree("libs"))
}

bukkitPluginYaml {
    main = "kr.toxicity.library.armormodel.test.TestPlugin"
    version = project.version.toString()
    name = "ArmorModel-TestPlugin"
    foliaSupported = true
    apiVersion = "1.20"
    author = "toxicity"
    description = "Test plugin of armor model."
}
