plugins {
    alias(kotlinLibs.plugins.jvm)
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    compileOnly(kotlinLibs.compiler)
}
