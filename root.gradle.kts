// https://github.com/EssentialGG/essential-gradle-toolkit
plugins {
    id("gg.essential.multi-version.root")
}

preprocess {
    val neoforge260100 = createNode("26.1-neoforge", 260100, "official")
    val fabric260100 = createNode("26.1-fabric", 260100, "official")
    val neoforge12111 = createNode("1.21.11-neoforge", 12111, "official")
    val fabric12111 = createNode("1.21.11-fabric", 12111, "yarn")
    val fabric12106 = createNode("1.21.6-fabric", 12106, "yarn")
    val neoforge12106 = createNode("1.21.6-neoforge", 12106, "official")
    val fabric12102 = createNode("1.21.2-fabric", 12102, "yarn")
    val neoforge12102 = createNode("1.21.2-neoforge", 12102, "official")
    val neoforge12101 = createNode("1.21.1-neoforge", 12101, "official")
    val fabric12101 = createNode("1.21.1-fabric", 12101, "yarn")

    neoforge260100.link(fabric260100)  // NeoForge 26.1    -> Fabric 26.1
    fabric260100.link(neoforge12111)   // Fabric 26.1      -> NeoForge 1.21.11
    neoforge12111.link(fabric12111)    // NeoForge 1.21.11 -> Fabric 1.21.11
    fabric12111.link(fabric12106)      // Fabric 1.21.11   ->  Fabric 1.21.6
    fabric12106.link(neoforge12106)    // Fabric 1.21.6    ->  NeoForge 1.21.6
    neoforge12106.link(neoforge12102)  // NeoForge 1.21.6  ->  NeoForge 1.21.2
    neoforge12102.link(fabric12102)    // NeoForge 1.21.2  ->  Fabric 1.21.2
    fabric12102.link(fabric12101)      // Fabric 1.21.2    ->  Fabric 1.21.1
    fabric12101.link(neoforge12101)    // Fabric 1.21.1    ->  NeoForge 1.21.1
}
