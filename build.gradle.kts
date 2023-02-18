plugins {
    id("build-parameters")

    alias(kotlinLibs.plugins.multiplatform) apply false
}

val skipTest = buildParameters.skip.test
val skipLink = buildParameters.skip.link
subprojects {
    if (skipTest) tasks.matching { it.name.endsWith("test", ignoreCase = true) }.configureEach { onlyIf { false } }
    if (skipLink) tasks.matching { it.name.startsWith("link", ignoreCase = true) }.configureEach { onlyIf { false } }
}
