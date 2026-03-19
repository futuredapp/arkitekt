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
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "decompose-annotation",
        version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT"
    )
    pom {
        name = "Arkitekt Decompose Annotation"
        description = "Annotations for Arkitekt Decompose module"
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
