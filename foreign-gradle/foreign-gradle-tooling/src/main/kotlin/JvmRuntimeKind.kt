package dev.whyoleg.foreign.gradle.tooling

public enum class JvmRuntimeKind {
    JNI, FFM, MR_JAR

    // TODO:
    //  we can also add BOTH:
    //  means both JNI and FFM will be in single jar
    //  and be used depending on JDK version or system property - is it needed?
}
