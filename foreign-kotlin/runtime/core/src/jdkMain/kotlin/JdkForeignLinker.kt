package dev.whyoleg.foreign

import java.lang.foreign.*
import java.lang.invoke.*
import kotlin.jvm.optionals.*

// lazy? Android? proguard?
public val JdkForeignAvailable: Boolean = run {
    val javaVersion = when (val specVersion = System.getProperty("java.specification.version")) {
        "1.8" -> 8
        else  -> specVersion.toInt()
    }
    javaVersion >= 22
}

// TODO: decide on name
@Suppress("Since15")
public object JdkForeignLinker {
    init {
        check(JdkForeignAvailable)
    }

    private val linker = Linker.nativeLinker()
    private val lookup = SymbolLookup.loaderLookup().or(linker.defaultLookup())

    private fun get(name: String): MemorySegment {
        return lookup.find(name).getOrNull() ?: error("Foreign function is not found: $name")
    }

    public fun methodHandle(
        name: String,
        descriptor: FunctionDescriptor,
    ): MethodHandle = linker.downcallHandle(get(name), descriptor)
}

//
//@ForeignMemoryApi
//public fun MemoryArena.ffmSegmentAllocator(): SegmentAllocator = when (this) {
//    is FfmMemoryArena -> allocator
//    else -> TODO("should not happen?")
//}
//
//@ForeignMemoryApi
//public fun MemoryBlock.ffmMemorySegment(): MemorySegment = when (this) {
//    is FfmMemoryBlock -> segment
//    else -> TODO("should not happen?") //MemorySegment.ofAddress(address, size)
//}
//
//@ForeignMemoryApi
//public fun MemoryBlockLayout.ffmMemoryLayout(): MemoryLayout = when (this) {
//    is FfmMemoryBlockLayout -> layout
//    is FfmMemoryBlockLayoutRecord -> layout
//    else -> TODO("should not happen?")
//}

//@ForeignMemoryApi
//internal class FfmMemoryBlockLayout(
//    val layout: MemoryLayout
//) : MemoryBlockLayout() {
//    override val size: MemoryAddressSize get() = layout.byteSize()
//    override val alignment: MemoryAddressSize get() = layout.byteAlignment()
//}
//
//@ForeignMemoryApi
//internal class FfmMemoryBlockLayoutRecord(
//    private val isUnion: Boolean
//) : MemoryBlockLayout.Record() {
//    private var fields: MutableList<MemoryLayout>? = mutableListOf()
//    private var _size: Long = 0
//    val layout: MemoryLayout by lazy {
//        val fields = checkNotNull(fields)
//        val layout = when (isUnion) {
//            true  -> MemoryLayout.unionLayout(*(fields.toTypedArray()))
//            false -> MemoryLayout.structLayout(*(fields.toTypedArray()))
//        }
//        this.fields = null
//
//        layout
//    }
//    override val size: MemoryAddressSize get() = layout.byteSize()
//    override val alignment: MemoryAddressSize get() = layout.byteAlignment()
//
//    override fun getOffsetAndAddField(layout: MemoryBlockLayout): MemoryAddressSize {
//        val fields = checkNotNull(fields) { "Layout already built" }
//        fields += layout.ffmMemoryLayout()
//
//        if (isUnion) return memoryAddressSizeZero()
//
//        //TODO: recheck which SIZE_BYTES is needed here
//        val padding = _size % kotlin.Long.SIZE_BYTES
//        if (padding != memoryAddressSizeZero() && padding < layout.size) {
//            _size += padding
//            fields += MemoryLayout.paddingLayout(padding * 8)
//        }
//
//        val offset = _size
//        _size += layout.size
//        return offset
//    }
//}
