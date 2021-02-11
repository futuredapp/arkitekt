plugins {
    kotlin("multiplatform")
    id("com.android.library")
    `maven-publish`
}

group = "app.futured.arkitekt"
version = "1.0.9-SNAPSHOT"

publishing {
    publications {
//        create<MavenPublication>("mavenJava") {
//            artifactId = "km-usecases"
//            description = "My test lib"
//            pom {
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("RudolfHladik")
//                        name.set("Rudolf Hladík")
//                        email.set("hladik.rudolf@gmail.com")
//                    }
//                }
//                scm {
//                    url.set("https://github.com/futuredapp/arkitekt")
//                }
//            }
//        }
        publications.withType<MavenPublication> {
            pom {
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("RudolfHladik")
                        name.set("Rudolf Hladík")
                        email.set("hladik.rudolf@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/futuredapp/arkitekt")
                }
            }
        }
    }
}

repositories {
    mavenLocal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android {
        publishAllLibraryVariants()
    }

    ios {
        binaries {
            framework {
                baseName = "km-usecases"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Kotlin.coroutinesMt)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}
