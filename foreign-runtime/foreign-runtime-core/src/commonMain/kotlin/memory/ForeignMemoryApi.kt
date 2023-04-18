package dev.whyoleg.foreign.memory

//TODO: move to root and rename to `UnsafeForeignApi`?
@Target(
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@RequiresOptIn
public annotation class ForeignMemoryApi
