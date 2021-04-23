buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
//        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.Kotlin.version}")
        classpath("com.android.tools.build:gradle:${Dependencies.Android.agp}")
        classpath(kotlin("serialization", version = Dependencies.Kotlin.version))
        classpath("com.apollographql.apollo:apollo-gradle-plugin:${Dependencies.Apollo.version}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Dependencies.SQLDelight.version}")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}
