import foreignbuild.dependencies.*
import org.gradle.kotlin.dsl.*

val setupTask = registerZipDependencySetupTask(
    "setupOpenssl3",
    "foreignbuild.openssl:openssl3-all:3.0.8-build-2@zip"
) {
    into(temporaryDir)
}

extensions.create<Openssl3Extension>(
    "openssl3",
    layout.dir(setupTask.map { it.destinationDir })
)
