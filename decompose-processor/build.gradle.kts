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
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "decompose-processor",
    )
    pom {
        name = "Arkitekt Decompose Processor"
        description = "KSP processor for Arkitekt Decompose annotations"
        url = "https://github.com/futuredapp/arkitekt"
        licenses {
            license {
                name = "MIT"
                url = "https://github.com/futuredapp/arkitekt/blob/master/LICENCE"
            }
        }
        scm {
            connection = "scm:git:git://github.com/futuredapp/arkitekt.git"
            developerConnection = "scm:git:ssh://github.com/futuredapp/arkitekt.git"
            url = "https://github.com/futuredapp/arkitekt"
        }
        developers {
            developer {
                id = "futured"
                name = "Futured"
                url = "https://futured.app"
            }
        }
    }
}
