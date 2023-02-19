plugins {
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(20) //for panama

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }

    sourceSets.all {
        val (targetName, compilationName) = name.run {
            val index = indexOfLast { it.isUpperCase() }
            take(index) to drop(index).lowercase()
        }
        kotlin.setSrcDirs(listOf("$compilationName/sources/$targetName"))
        resources.setSrcDirs(listOf("$compilationName/resources/$targetName"))

        languageSettings {
            progressiveMode = true
            optIn("kotlin.ExperimentalStdlibApi")
        }

        if (compilationName == "test") when (targetName) {
            "common" -> "test"
            "jvm"    -> "test-junit"
            "js"     -> "test-js"
            else     -> null
        }?.let { testLibrary ->
            dependencies {
                implementation(kotlin(testLibrary))
            }
        }
    }
}
