import foreignbuild.dependencies.*

val zuluJdkMajorVersion = "zulu-8"
val zuluJdkVersion = "zulu8.72.0.17-ca-jdk8.0.382"

registerSetupJdkHeadersTask(
    os = "LinuxX64",
    osClassifier = "linux_x64",
    includesRoot = "include"
)

registerSetupJdkHeadersTask(
    os = "MacosX64",
    osClassifier = "macosx_x64",
    includesRoot = "$zuluJdkMajorVersion.jdk/Contents/Home/include"
)

registerSetupJdkHeadersTask(
    os = "MacosArm64",
    osClassifier = "macosx_aarch64",
    includesRoot = "$zuluJdkMajorVersion.jdk/Contents/Home/include"
)

// TODO: windows

fun registerSetupJdkHeadersTask(
    os: String,
    osClassifier: String,
    includesRoot: String
) = registerZipDependencySetupTask(
    "setupJdkHeaders$os",
    "foreignbuild.jdk:$osClassifier:$zuluJdkVersion@zip"
) {
    include("$zuluJdkVersion-$osClassifier/$includesRoot/**/*.h")
    eachFile {
        relativePath = RelativePath.parse(true, relativePath.pathString.substringAfter("include/"))
    }
}
