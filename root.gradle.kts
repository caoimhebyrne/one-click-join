// https://github.com/EssentialGG/essential-gradle-toolkit
plugins {
    id("gg.essential.multi-version.root")
}

preprocess {
    val fabric12102 = createNode("1.21.2-fabric", 12102, "yarn")
    val neoforge12102 = createNode("1.21.2-neoforge", 12102, "official")
    val neoforge12101 = createNode("1.21.1-neoforge", 12101, "official")
    val fabric12101 = createNode("1.21.1-fabric", 12101, "yarn")

    fabric12102.link(neoforge12102)    // Fabric 1.21.2    ->  NeoForge 1.21.2
    neoforge12102.link(neoforge12101)  // NeoForge 1.21.2  ->  NeoForge 1.21.1
    neoforge12101.link(fabric12101)    // NeoForge 1.21.1  ->  Fabric 1.21.1
}
