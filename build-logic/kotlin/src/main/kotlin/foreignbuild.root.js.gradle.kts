import org.jetbrains.kotlin.gradle.targets.js.nodejs.*
import org.jetbrains.kotlin.gradle.targets.js.yarn.*

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
