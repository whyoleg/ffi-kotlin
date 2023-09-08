package dev.whyoleg.foreign.cx.playground.main

import dev.whyoleg.foreign.cx.index.generator.*
import dev.whyoleg.foreign.cx.playground.*
import dev.whyoleg.foreign.cx.playground.CxIndexGeneratorTarget
import okio.*
import okio.ByteString.Companion.encodeUtf8
import okio.Path.Companion.toPath

public fun main() {
    val headersDir = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3"
    val outputDir = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tools/foreign-cx-playground/build/foreign/time/"
    val headerFilePath = createTemporaryHeadersFile(
        setOf(
            //"openssl/evp.h",
            "time.h"
        )
    )

    mapOf(
        "mingw-x64" to CxIndexGeneratorTarget.MingwX64,
        "linux-x64" to CxIndexGeneratorTarget.LinuxX64,
        "macos-x64" to CxIndexGeneratorTarget.MacosX64,
        "macos-arm64" to CxIndexGeneratorTarget.MacosArm64,
        "ios-device-arm64" to CxIndexGeneratorTarget.IosDeviceArm64,
        "ios-simulator-arm64" to CxIndexGeneratorTarget.IosSimulatorArm64,
        "ios-simulator-x64" to CxIndexGeneratorTarget.IosSimulatorX64,
    ).forEach { (targetName, target) ->
        println("RUN: $targetName")
        val index = generateCxIndex(
            headerFilePath,
            target.arguments(
                CxIndexGeneratorTarget.Host.MacosArm64,
                CxIndexGeneratorTarget.KonanDirectory("/Users/whyoleg/.konan")
            ) + listOf("-I$headersDir/${targetName}/include")
        )
        val output = "$outputDir/$targetName"
        SystemFileSystem.writeCxIndex("$output.json".toPath(), index)
        SystemFileSystem.writeCxIndexVerbose(output.toPath(), index)
    }
}

internal fun createTemporaryHeadersFile(headers: Set<String>): String {
    val fileInfo = headers.joinToString("\n") { "#include <$it>" }
    val hashInfo = fileInfo.encodeUtf8().sha1().hex()
    val headersPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("$hashInfo.h")
    val headersExists = SystemFileSystem.exists(headersPath)
    if (!headersExists) SystemFileSystem.write(headersPath, mustCreate = true) { writeUtf8(fileInfo) }
    return headersPath.toString()
}
