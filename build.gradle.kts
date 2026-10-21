import me.modmuss50.mpp.ReleaseType
import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.api.artifacts.Configuration

plugins {
    id("gg.essential.defaults")
    id("gg.essential.multi-version")
    id("me.modmuss50.mod-publish-plugin")
}

fun Project.dependencyVersion(name: String, friendlyName: String = name, defaultValue: String? = null): String {
    return this.findProperty("dependency.$name.version") as? String
        ?: defaultValue
        ?: error("No $friendlyName version defined for ${platform.mcVersionStr} (${platform.loaderStr})")
}

fun Configuration.dependencyVersion(group: String, name: String): String {
    return dependencies.single { it.group == group && it.name == name }.version
        ?: error("No version defined for $group:$name in configuration ${this@dependencyVersion.name}")
}

group = "dev.caoimhe"
version = "1.3.1"
base.archivesName = "one-click-join-${project.name}"

repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com")

    // Loom (or some side effect of the preprocessor plugin) causes NeoForge dependencies to be resolved from within,
    // Fabric subprojects so every subproject needs access to the NeoForged maven.
    maven("https://maven.neoforged.net/releases")
}

dependencies {
    // Only used in the development environment for authentication.
    val devAuthVersion = dependencyVersion("devauth", "DevAuth")
    modRuntimeOnly("me.djtheredstoner:DevAuth-${platform.loaderStr}:$devAuthVersion")

    if (platform.isFabric) {
        // val modMenuVersion = dependencyVersion("modmenu", "ModMenu")
        val fabricApiVersion = dependencyVersion("fabric-api", "Fabric API")

        // modRuntimeOnly("com.terraformersmc:modmenu:$modMenuVersion")
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    }
}

val javaVersion = if (platform.mcVersion >= 260100) JavaVersion.VERSION_25 else JavaVersion.VERSION_21

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks {
    jar {
        // This gradle script is evaluated for each `./version/{id}`, in order to get the LICENSE file, we need to
        // resolve it from the root project, not the version's root.
        from(rootProject.file("LICENSE")) {
            rename { "one-click-join_${it}" }
        }
    }
}

val finalJarTask = if (platform.isUnobfuscated) {
    tasks.jar
} else {
    tasks.named<RemapJarTask>("remapJar")
}

val supportedMinecraftVersions = buildSet {
    add(platform.mcVersionStr)

    val extraMinecraftVersions = project.findProperty("dev.caoimhe.extra-minecraft-versions") as? String
        ?: return@buildSet

    addAll(extraMinecraftVersions.split(",").map { it.trim() }.filter { it.isNotEmpty() })
}

val commonResourceProperties = mapOf(
    "version" to project.version.toString(),
    "java_version" to javaVersion.majorVersion
)

val loaderResourceProperties = when {
    platform.isFabric -> {
        val fabricLoaderVersion = configurations["modImplementation"].dependencyVersion("net.fabricmc", "fabric-loader")
        mapOf(
            "fabric_loader_version" to fabricLoaderVersion,
            "minecraft_versions_array" to supportedMinecraftVersions.joinToString(",", "[", "]") { "\"$it\"" }
        )
    }

    platform.isNeoForge -> {
        val neoForgeVersion = configurations["neoForge"].dependencyVersion("net.neoforged", "neoforge")
        mapOf(
            "neoforge_version_range" to "[$neoForgeVersion,)",
            // NeoForge uses a union of singleton Maven ranges, not a list of versions.
            "minecraft_versions_range" to supportedMinecraftVersions.joinToString(",") { "[$it]" }
        )
    }

    else -> error("Unsupported loader: ${platform.loaderStr}")
}

val resourceProperties = commonResourceProperties + loaderResourceProperties

tasks {
    processResources {
        inputs.properties(resourceProperties)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(resourceProperties)
        }
    }
}

publishMods {
    type = ReleaseType.STABLE
    displayName = version
    changelog = providers.fileContents(rootProject.layout.projectDirectory.file("release-notes.md")).asText
    modLoaders = listOf(platform.loaderStr)
    dryRun = providers.gradleProperty("publishing.disable-dry-run").orNull != "true"

    modrinth {
        projectId = "gmVdcG6g"
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions = supportedMinecraftVersions
        environment = ModrinthEnvironment.CLIENT_ONLY

        if (platform.isFabric) {
            requires("fabric-api")
        }
    }

    file = finalJarTask.get().archiveFile
}
