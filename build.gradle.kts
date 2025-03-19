plugins {
    id("gg.essential.defaults")
    id("gg.essential.multi-version")
}

group = "dev.caoimhe"
version = "1.0.0-alpha.1"
base.archivesName = "one-click-join-${project.name}"

repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
    // This is only used in the development environment for authentication.
    modRuntimeOnly("me.djtheredstoner:DevAuth-${platform.loaderStr}:1.2.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

loom {
    runConfigs {
        remove(named("server").get())
    }
}
