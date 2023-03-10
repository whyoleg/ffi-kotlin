import org.jetbrains.kotlin.gradle.targets.js.nodejs.*
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.*
import org.jetbrains.kotlin.gradle.targets.js.yarn.*

plugins {
    id("build-parameters")

    alias(kotlinLibs.plugins.multiplatform) apply false
    id("com.android.library") version "7.3.0" apply false //TODO move to version catalog
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().apply {
        yarnLockMismatchReport = YarnLockMismatchReport.NONE
        yarnLockAutoReplace = true
    }
}

plugins.withType<NodeJsRootPlugin> {
    the<NodeJsRootExtension>().apply {
        //from https://github.com/skuzmich/kotlin-wasi-bindings-experiments
        nodeVersion = "20.0.0-v8-canary2022112061c569ba0d"
        nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
    }
}
tasks.withType<KotlinNpmInstallTask> {
    args.add("--ignore-engines")
}
val skipTest = buildParameters.skip.test
val skipLink = buildParameters.skip.link
subprojects {
    if (skipTest) tasks.matching { it.name.endsWith("test", ignoreCase = true) }.configureEach { onlyIf { false } }
    if (skipLink) tasks.matching { it.name.startsWith("link", ignoreCase = true) }.configureEach { onlyIf { false } }
}
