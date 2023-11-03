package dev.whyoleg.foreign.gradle.internal

//internal abstract class DefaultForeignCxInterop @Inject constructor(
//    val interfaceName: String,
//    objectFactory: ObjectFactory
//) : DefaultBaseCxInterop(objectFactory, null),
//    ForeignCxInterop {
//    override fun getName(): String = interfaceName
//
//    abstract override val bindings: DefaultRootForeignCxInterfaceConfigurationBindings
//
//    val platforms = objectFactory.polymorphicDomainObjectContainer(
//        DefaultPlatformCxInterop::class
//    ).apply {
//        registerFactory(
//            DefaultJvmPlatformCxInterop::class.java,
//            DefaultJvmPlatformCxInterop.Factory(
//                objectFactory,
//                this@DefaultForeignCxInterop
//            )
//        )
//    }
//
//    init {
//        run {
//            sourceSetTree.convention(KotlinSourceSetTree.main)
//            bindings.packageName.convention { "TODO" }
//        }
//    }
//
//    override fun jvm(configure: JvmPlatformCxInterop.() -> Unit) {
//        platforms.maybeCreate("jvm", DefaultJvmPlatformCxInterop::class).apply(configure)
//    }
//
//    override fun native(configure: NativePlatformCxInterop.() -> Unit) {
////        platforms.maybeCreate("native", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
//    }
//
//    override fun android(configure: AndroidPlatformCxInterop.() -> Unit) {
////        platforms.maybeCreate("android", DefaultJvmPlatformForeignCxInterfaceConfiguration::class).apply(configure)
//    }
//}

