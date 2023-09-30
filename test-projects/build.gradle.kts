plugins {
    id("foreignbuild.root.js")
    alias(kotlinLibs.plugins.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
}
