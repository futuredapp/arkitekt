plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.10.0"
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
    website = "https://github.com/thefuntasty/mvvm-android"
    vcsUrl = "https://github.com/thefuntasty/mvvm-android"
    tags = listOf("android", "template", "mvvm")
}
gradlePlugin {
    plugins {
        create(ProjectSettings.Templates.name) {
            id = ProjectSettings.Templates.id
            displayName = "The Funtasty MVVM Android Studio templates"
            description = "This will add option to create new MVVM Fragment an MVVM Activity.\n" +
                "Multiple files required by MVVM framework will be automatically generated (see: https://github.com/thefuntasty/mvvm-android)"
            implementationClass = ProjectSettings.Templates.implementationClass
        }
    }
}

tasks.register("install") {
    dependsOn("build")
    dependsOn("publishToMavenLocal")
}
