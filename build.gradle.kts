// Top-level build file where you can add configuration options common to all sub-projects/modules.
//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id("com.android.application") version "8.1.2" apply false
//    id("org.jetbrains.kotlin.kapt") version "2.0.0-Beta1"
//}
//

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10") // Update to the latest version
    }
}

allprojects {
    repositories {
        // google()
        //  mavenCentral()
    }
}