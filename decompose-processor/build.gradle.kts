plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        jvmMain  {
            dependencies {
                implementation(project(":decompose-annotation"))
                implementation(Deps.Ksp.api)
                implementation(Deps.Poet.interop)
            }
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "decompose-processor")
    pom {
        arkitektPomBase()
        name = "Arkitekt Decompose Processor"
        description = "KSP processor for Arkitekt Decompose annotations"
        inceptionYear = "2026"
    }
}
