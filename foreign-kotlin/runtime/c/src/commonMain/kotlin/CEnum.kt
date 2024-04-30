@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

// name clash if constant is named `Companion`
public interface CEnum {
    public val value: Int
}

// TODO: check pointers and arrays with enums

//@JvmInline
//public value class SOME_ENUM(override val value: Int) : CEnum {
//    public companion object
//}
//
//public inline val SOME_ENUM.Companion.Companion: SOME_ENUM get() = SOME_ENUM(1)
//public inline val SOME_ENUM.Companion.s: SOME_ENUM get() = SOME_ENUM(1)
//
//
//private val s: SOME_ENUM = SOME_ENUM.Companion.Companion
//private val s2: SOME_ENUM = SOME_ENUM.s
