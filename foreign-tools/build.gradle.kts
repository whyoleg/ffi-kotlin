plugins {
    id("foreignbuild.conventions.multiplatform.root")
    alias(kotlinLibs.plugins.multiplatform) apply false
    alias(libs.plugins.undercouch.download) apply false
}
