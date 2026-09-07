plugins {
    id("fabric-loom") version "1.16-SNAPSHOT"
    id("maven-publish")
}

version = project.property("mod_version").toString()
group = project.property("maven_group").toString()

base {
    archivesName.set(project.property("archives_base_name").toString())
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://api.modrinth.com/maven") }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    // Cloth Config
    modApi("me.shedaniel.cloth:cloth-config-fabric:15.0.127") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    // ModMenu
    modImplementation("maven.modrinth:modmenu:11.0.3")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))

    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version")
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${archiveBaseName.get()}" }
    }
}