plugins {
    id("gg.essential.defaults")
    id("gg.essential.multi-version")
}

group = "dev.caoimhe"
version = "1.0.0-alpha.1"
base.archivesName = "one-click-join-${project.name}"

repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // Only used in the development environment for authentication.
    modRuntimeOnly("me.djtheredstoner:DevAuth-${platform.loaderStr}:1.2.1")

    if (platform.isFabric) {
        // TODO: Add a property to each version's project that defines these versions.
        //       e.g. `dependency.modmenu.version`, `dependency.fabric-api.version`, etc.
        val modMenuVersion = when (platform.mcVersion) {
            12101 -> "11.0.3"
            12102 -> "12.0.0"
            else -> error("No ModMenu version defined for ${platform.mcVersionStr}")
        }

        val fabricApiVersion = when (platform.mcVersion) {
            12101 -> "0.115.3+1.21.1"
            12102 -> "0.106.1+1.21.2"
            else -> error("No Fabric API version defined for ${platform.mcVersionStr}")
        }

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
