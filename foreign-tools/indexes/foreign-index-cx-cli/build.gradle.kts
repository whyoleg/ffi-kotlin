import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*
import org.jetbrains.kotlin.konan.target.*
import org.tukaani.xz.*

plugins {
    id("foreignbuild.conventions.multiplatform.base")
    id("foreignbuild.conventions.multiplatform.targets.native.desktop")
    alias(kotlinLibs.plugins.multiplatform)
    alias(libs.plugins.undercouch.download)
}

buildscript {
    dependencies {
        // TODO: move to version catalog
        // for xz archives
        classpath("org.tukaani:xz:1.9")
    }
}

// download and extract clang headers
val downloadClangSources by tasks.registering(Download::class) {
    src("https://github.com/llvm/llvm-project/releases/download/llvmorg-11.1.0/clang-11.1.0.src.tar.xz")
    dest(layout.buildDirectory.file("clang/sources.tar.xz"))
    overwrite(false)
}

val uncompressedFile = layout.buildDirectory.file("clang/sources.tar")
val uncompressClangSources by tasks.registering {
    inputs.file(downloadClangSources.map { it.dest })
    outputs.file(uncompressedFile)

    doLast {
        val destination = uncompressedFile.get().asFile
        destination.delete()
        destination.outputStream().use { output ->
            XZInputStream(downloadClangSources.get().dest.inputStream()).use { input ->
                input.copyTo(output)
            }
        }
    }
}

val unzipClangCHeaders by tasks.registering(Sync::class) {
    dependsOn(uncompressClangSources)
    from(tarTree(uncompressedFile)) {
        include("clang-11.1.0.src/include/clang-c/*.h")
        eachFile {
            relativePath = RelativePath(true, "clang-c", name)
        }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("clang/include"))
}

val downloadMacosArm64LLVM by tasks.registering(Download::class) {
    src("https://download.jetbrains.com/kotlin/native/apple-llvm-20200714-macos-aarch64-essentials.tar.gz")
    dest(layout.buildDirectory.file("llvm/macos-arm64.tar.gz"))
    overwrite(false)
}

val unzipMacosArm64LLVM by tasks.registering(Sync::class) {
    dependsOn(downloadMacosArm64LLVM)
    from(tarTree(resources.gzip(downloadMacosArm64LLVM.map { it.dest }))) {
        include("apple-llvm-20200714-macos-aarch64-essentials/lib/*.dylib")
        eachFile {
            relativePath = RelativePath(true, name)
        }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("llvm/lib/macos-arm64"))
}

val downloadMacosX64LLVM by tasks.registering(Download::class) {
    src("https://download.jetbrains.com/kotlin/native/apple-llvm-20200714-macos-x64-essentials.tar.gz")
    dest(layout.buildDirectory.file("llvm/macos-x64.tar.gz"))
    overwrite(false)
}

val unzipMacosX64LLVM by tasks.registering(Sync::class) {
    dependsOn(downloadMacosX64LLVM)
    from(tarTree(resources.gzip(downloadMacosX64LLVM.map { it.dest }))) {
        include("apple-llvm-20200714-macos-x64-essentials/lib/*.dylib")
        eachFile {
            relativePath = RelativePath(true, name)
        }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("llvm/lib/macos-x64"))
}

val downloadLinuxX64LLVM by tasks.registering(Download::class) {
    src("https://download.jetbrains.com/kotlin/native/llvm-11.1.0-linux-x64-essentials.tar.gz")
    dest(layout.buildDirectory.file("llvm/linux-x64.tar.gz"))
    overwrite(false)
}

val unzipLinuxX64LLVM by tasks.registering(Sync::class) {
    dependsOn(downloadLinuxX64LLVM)
    from(tarTree(resources.gzip(downloadLinuxX64LLVM.map { it.dest }))) {
        include("llvm-11.1.0-linux-x64-essentials/lib/*.so.11.1")
        eachFile {
            relativePath = RelativePath(true, "libclang.so")
        }
        includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("llvm/lib/linux-x64"))
}

tasks.withType<CInteropProcess>().configureEach {
    dependsOn(unzipClangCHeaders)
}

kotlin {
    targets.withType(KotlinNativeTargetWithHostTests::class).configureEach {
        compilations.named("main") {
            cinterops.create("declarations") {
                defFile("src/commonMain/interop/clang.def")
                includeDirs(unzipClangCHeaders.map { it.destinationDir })
            }
        }

        compilations.configureEach {
            compilerOptions.configure {
                val syncTask = when (konanTarget) {
                    KonanTarget.MACOS_ARM64 -> unzipMacosArm64LLVM
                    KonanTarget.MACOS_X64   -> unzipMacosX64LLVM
                    KonanTarget.LINUX_X64   -> unzipLinuxX64LLVM
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

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.indexes.foreignIndexCx)
                implementation(libs.clikt)
            }
        }
    }
}
