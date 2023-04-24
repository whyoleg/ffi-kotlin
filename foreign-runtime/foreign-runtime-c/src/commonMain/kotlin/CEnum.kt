package dev.whyoleg.foreign.c

import kotlin.jvm.*

// just interfaces, to keep contract
public interface CEnum<E : Enum<E>, V : CEnum.Value<E, V>> {
    public val value: V

    // Value interface should be overridden by `value class`
    // and all methods should be declared there to avoid boxing
    public interface Value<E : Enum<E>, V : Value<E, V>> {
        public val underlying: Int

        public fun toEnum(): E
        public fun toEnumOrNull(): E?
    }

    //Simple cache + methods, that should be used by Value class
    //TODO: decide on better caching, as it can create BIG arrays without requirement for it, if `underlying` values are big
    public abstract class Cache<E : Enum<E>, V : Value<E, V>>(values: Array<out CEnum<E, V>>) {
        private val enums: Array<E?> = arrayOfNulls<Any?>(values.maxOf { it.value.underlying }).apply {
            values.forEach { set(it.value.underlying, it) }
        } as Array<E?>

        protected fun get(underlying: Int): E = checkNotNull(enums[underlying]) { "No enum with value '$underlying'" }
        protected fun getOrNull(underlying: Int): E? = enums[underlying]
        protected fun toString(underlying: Int, enumName: String): String = when (val enum = getOrNull(underlying)) {
            null -> "$enumName.Value(underlying=$underlying)"
            else -> "$enumName.Value(${enum.name})"
        }
    }
}
