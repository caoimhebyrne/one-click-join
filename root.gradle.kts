// https://github.com/EssentialGG/essential-gradle-toolkit
plugins {
    id("gg.essential.loom") version "1.7.28" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val fabric12106 = createNode("1.21.6-fabric", 12106, "yarn")
    val neoforge12106 = createNode("1.21.6-neoforge", 12106, "official")
    val fabric12102 = createNode("1.21.2-fabric", 12102, "yarn")
    val neoforge12102 = createNode("1.21.2-neoforge", 12102, "official")
    val neoforge12101 = createNode("1.21.1-neoforge", 12101, "official")
    val fabric12101 = createNode("1.21.1-fabric", 12101, "yarn")

    fabric12106.link(neoforge12106)    // Fabric 1.21.6    ->  NeoForge 1.21.6
    neoforge12106.link(neoforge12102)  // NeoForge 1.21.6  ->  NeoForge 1.21.2
    neoforge12102.link(fabric12102)    // NeoForge 1.21.2  ->  Fabric 1.21.2
    fabric12102.link(fabric12101)      // Fabric 1.21.2    ->  Fabric 1.21.1
    fabric12101.link(neoforge12101)    // Fabric 1.21.1    ->  NeoForge 1.21.1
}
