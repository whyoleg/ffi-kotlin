package foreignbuild.conventions.multiplatform

import org.jetbrains.kotlin.gradle.targets.js.nodejs.*
import org.jetbrains.kotlin.gradle.targets.js.yarn.*

plugins {
    id("build-parameters")
}

plugins.withType<YarnPlugin>().configureEach {
    the<YarnRootExtension>().apply {
        yarnLockMismatchReport = YarnLockMismatchReport.NONE
        yarnLockAutoReplace = true
        lockFileDirectory = project.layout.buildDirectory.dir("kotlin-js-store").get().asFile
    }
}

plugins.withType<NodeJsRootPlugin>().configureEach {
    the<NodeJsRootExtension>().apply {
        nodeVersion = "20.5.1"
    }
}

val skipTest = buildParameters.skip.test
val skipLink = buildParameters.skip.link

subprojects {
    if (skipTest) tasks.matching { it.name.endsWith("test", ignoreCase = true) }.configureEach { onlyIf { false } }
    if (skipLink) tasks.matching { it.name.startsWith("link", ignoreCase = true) }.configureEach { onlyIf { false } }
}
