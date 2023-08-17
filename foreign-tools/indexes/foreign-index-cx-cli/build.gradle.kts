import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("foreignbuild.conventions.multiplatform.base")
    id("foreignbuild.conventions.multiplatform.targets.jvm")
    id("foreignbuild.conventions.multiplatform.targets.native.desktop")
    id("foreignbuild.dependencies.libclang")
    alias(kotlinLibs.plugins.multiplatform)
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(tasks.setupLibclangHeaders)
}

val prepareJvmResources by tasks.registering(Sync::class) {
    // TODO: add other OSs here
    from(tasks.setupLibclangMacosArm64.map { it.destinationDir }) {
        into("cli/macos-arm64/lib")
    }
    from(kotlin.targets.withType(KotlinNativeTarget::class).matching {
        it.konanTarget == KonanTarget.MACOS_ARM64
    }.map {
        it.binaries.getExecutable(NativeBuildType.RELEASE).linkTaskProvider.map { it.outputFile }
    }) {
        into("cli/macos-arm64/bin")
    }

    into(layout.buildDirectory.dir("jvmResources"))
}


@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    targets.withType(KotlinNativeTargetWithHostTests::class).configureEach {
        compilations.named("main") {
            cinterops.create("declarations") {
                defFile("src/nativeMain/interop/clang.def")
                includeDirs(tasks.setupLibclangHeaders.map { it.destinationDir })
            }
        }

        compilations.configureEach {
            compilerOptions.configure {
                val syncTask = when (konanTarget) {
                    KonanTarget.MACOS_ARM64 -> tasks.setupLibclangMacosArm64
                    KonanTarget.MACOS_X64   -> tasks.setupLibclangMacosX64
                    KonanTarget.LINUX_X64   -> tasks.setupLibclangLinuxX64
                    else                    -> TODO("Not supported: $konanTarget")
                }
                freeCompilerArgs.add("-linker-option")
                freeCompilerArgs.add(syncTask.map { "-L${it.destinationDir}" })
            }
        }

        binaries.executable {
            entryPoint = "dev.whyoleg.foreign.index.cx.cli.main"
        }
    }

    jvm {
        mainRun {
            mainClass.set("dev.whyoleg.foreign.index.cx.cli.MainKt")
        }
    }

    sourceSets {
        named("jvmMain") {
            resources.srcDir(prepareJvmResources.map { it.destinationDir })
        }
        named("nativeMain") {
            dependencies {
                implementation(projects.indexes.foreignIndexCx)
                implementation(libs.clikt)
            }
        }
    }
}
