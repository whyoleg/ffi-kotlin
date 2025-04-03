import foreignbuild.*

plugins {
    id("foreignbuild.kotlin-tool")

    id("foreignbuild.setup-openssl")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.foreignToolClang)
        }
    }
}

registerGenerateConstantsTask("generateTestenvConstants", kotlin.sourceSets.commonMain) {
    packageName = "dev.whyoleg.foreign.tool.clang.testenv"
    className = "TestenvConstants"
    properties.put(
        "OPENSSL3_ROOT_PATH",
        openssl.v3_2.setupTask.map { it.outputDirectory.get().asFile.absolutePath }
    )
}
