plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.11.0"
}

project.setProperty("archivesBaseName", ProjectSettings.Templates.module)

version = ProjectSettings.version
group = ProjectSettings.group

repositories {
    jcenter()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

pluginBundle {
    website = "https://github.com/futuredapp/arkitekt"
    vcsUrl = "https://github.com/futuredapp/arkitekt"
    tags = listOf("android", "template", "mvvm")
}
gradlePlugin {
    plugins {
        create(ProjectSettings.Templates.name) {
            id = ProjectSettings.Templates.id
            displayName = "The Arkitekt Android Studio templates"
            description = "This will add option to create new Arkitekt Fragment an Arkitekt Activity.\n" +
                "Multiple files required by Arkitekt framework will be automatically generated (see: https://github.com/futuredapp/arkitekt)"
            implementationClass = ProjectSettings.Templates.implementationClass
        }
    }
}

tasks.register("install") {
    dependsOn("build")
    dependsOn("publishToMavenLocal")
}
