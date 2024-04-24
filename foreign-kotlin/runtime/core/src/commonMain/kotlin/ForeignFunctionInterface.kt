package dev.whyoleg.foreign

// default annotation for generated bindings
@RequiresOptIn
public annotation class ForeignFunctionInterface

// TODO: better name and description
@RequiresOptIn("Available not on all OS")
public annotation class PartialForeignFunctionInterface(
    public val availableOn: Array<String>
)
