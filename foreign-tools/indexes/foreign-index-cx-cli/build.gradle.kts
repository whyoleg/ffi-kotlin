import de.undercouch.gradle.tasks.download.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.tasks.*
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

val unzipClangCHeaders by tasks.registering(Copy::class) {
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
