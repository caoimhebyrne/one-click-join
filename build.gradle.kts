plugins {
    id("gg.essential.defaults")
    id("gg.essential.multi-version")
}

fun Project.dependencyVersion(name: String, friendlyName: String = name, defaultValue: String? = null): String {
    return this.findProperty("dependency.$name.version") as? String
        ?: defaultValue
        ?: error("No $friendlyName version defined for ${platform.mcVersionStr} (${platform.loaderStr})")
}

group = "dev.caoimhe"
version = "1.0.1"
base.archivesName = "one-click-join-${project.name}"

repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // Only used in the development environment for authentication.
    val devAuthVersion = dependencyVersion("devauth", "DevAuth")
    modRuntimeOnly("me.djtheredstoner:DevAuth-${platform.loaderStr}:$devAuthVersion")

    if (platform.isFabric) {
        val modMenuVersion = dependencyVersion("modmenu", "ModMenu")
        val fabricApiVersion = dependencyVersion("fabric-api", "Fabric API")

        modRuntimeOnly("com.terraformersmc:modmenu:$modMenuVersion")
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

loom {
    runConfigs {
        remove(named("server").get())

        // The Nvidia drivers on Fedora Linux are broken, this fixes the issue of the game hanging upon exit for me,
        // if this causes issues in the future it can be removed, but it's nicer to have it defined here for now.
        named("client") {
            environmentVariable("__GL_THREADED_OPTIMIZATIONS", 0)
        }
    }
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
