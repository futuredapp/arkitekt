import org.gradle.api.publish.maven.MavenPom

/**
 * Applies the shared Arkitekt POM metadata (url, licenses, scm, developers) to this pom.
 *
 * Per-module values (name, description, inceptionYear) are configured separately and may be
 * set before or after invoking this function.
 */
fun MavenPom.arkitektPomBase() {
    url.set("https://github.com/futuredapp/arkitekt")
    licenses {
        license {
            name.set("MIT")
            url.set("https://github.com/futuredapp/arkitekt/blob/master/LICENCE")
        }
    }
    scm {
        connection.set("scm:git:git://github.com/futuredapp/arkitekt.git")
        developerConnection.set("scm:git:ssh://github.com/futuredapp/arkitekt.git")
        url.set("https://github.com/futuredapp/arkitekt")
    }
    developers {
        developer {
            id.set("futured")
            name.set("Futured")
            url.set("https://futured.app")
        }
    }
}
