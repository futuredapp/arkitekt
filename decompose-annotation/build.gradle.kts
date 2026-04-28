plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "decompose-annotation")
    pom {
        arkitektPomBase()
        name = "Arkitekt Decompose Annotation"
        description = "Annotations for Arkitekt Decompose module"
        inceptionYear = "2026"
    }
}
