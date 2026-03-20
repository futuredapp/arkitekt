package app.futured.arkitekt

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.artifacts.ComponentSelection

abstract class DependencyUpdates : DependencyUpdatesTask() {

    init {
        group = "arkitekt"

        this.resolutionStrategy {
            componentSelection {
                all { selection: ComponentSelection ->
                    val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "testing")
                        .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                        .any { it.matches(selection.candidate.version) }
                    if (rejected) {
                        selection.reject("Release candidate")
                    }
                }
            }
        }
    }
}
