import foreignbuild.openssl.*

fun configureOpensslXExtension(classifier: String, version: String, tag: String): OpensslXExtension {
    val configuration = configurations.create("openssl_$classifier")
    dependencies {
        configuration("foreignbuild.openssl:openssl-$version:$tag@zip")
    }

    val setupOpenssl = tasks.register<UnzipTask>("setupOpenssl_$classifier") {
        inputFile.set(project.layout.file(provider { configuration.singleFile }))
        outputDirectory.set(temporaryDir)
    }

    return OpensslXExtension(setupOpenssl)
}

val extension = OpensslExtension(
    v3_0 = configureOpensslXExtension("v3_0", "3.0.12", "3.0.12_1"),
    v3_1 = configureOpensslXExtension("v3_1", "3.1.4", "3.1.4_1"),
    v3_2 = configureOpensslXExtension("v3_2", "3.2.0", "3.2.0_1"),
)

extensions.add("openssl", extension)
