import foreignbuild.external.*

val zuluJdkMajorVersion = "zulu-8"
val zuluJdkVersion = "zulu8.72.0.17-ca-jdk8.0.382"

val setupJdkHeadersLinuxX64 by registerSetupJdkHeadersTask(
    os = "linux_x64",
    includesRoot = "include"
)

val setupJdkHeadersMacosX64 by registerSetupJdkHeadersTask(
    os = "macosx_x64",
    includesRoot = "$zuluJdkMajorVersion.jdk/Contents/Home/include"
)

val setupJdkHeadersMacosArm64 by registerSetupJdkHeadersTask(
    os = "macosx_aarch64",
    includesRoot = "$zuluJdkMajorVersion.jdk/Contents/Home/include"
)

fun registerSetupJdkHeadersTask(
    os: String,
    includesRoot: String
) = tasks.registering(SetupZipDependency::class) {
    dependency("foreignbuild.jdk:$os:$zuluJdkVersion@zip")
    include("$zuluJdkVersion-$os/$includesRoot/**/*.h")
    eachFile {
        relativePath = RelativePath.parse(true, relativePath.pathString.substringAfter("include/"))
    }
    includeEmptyDirs = false
    into(temporaryDir)
}
