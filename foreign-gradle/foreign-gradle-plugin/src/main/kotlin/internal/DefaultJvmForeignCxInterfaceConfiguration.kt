package dev.whyoleg.foreign.gradle.internal

//internal class DefaultJvmPlatformCxInterop(
//    platformName: String,
//    objectFactory: ObjectFactory,
//    parent: DefaultForeignCxInterop
//) : DefaultPlatformCxInterop(platformName, objectFactory, parent),
//    JvmPlatformCxInterop {
//
//    override val runtimeKind: Property<RuntimeKind> = objectFactory.property<RuntimeKind>().convention(RuntimeKind.JNI)
//
//    val hosts = objectFactory.domainObjectContainer(
//        DefaultJvmTargetCxInterop::class,
//        DefaultJvmTargetCxInterop.Factory(objectFactory, this)
//    )
//
//    override fun macosArm64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("macosArm64").apply(configure)
//    }
//
//    override fun macosX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("macosX64").apply(configure)
//    }
//
//    override fun linuxX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("linuxX64").apply(configure)
//    }
//
//    override fun mingwX64(configure: JvmTargetCxInterop.() -> Unit) {
//        hosts.maybeCreate("mingwX64").apply(configure)
//    }
//
//    class Factory(
//        private val objectFactory: ObjectFactory,
//        private val parent: DefaultForeignCxInterop
//    ) : NamedDomainObjectFactory<DefaultJvmPlatformCxInterop> {
//        override fun create(name: String): DefaultJvmPlatformCxInterop =
//            DefaultJvmPlatformCxInterop(name, objectFactory, parent)
//    }
//}
//
//internal class DefaultJvmTargetCxInterop(
//    name: String,
//    objectFactory: ObjectFactory,
//    parent: DefaultJvmPlatformCxInterop
//) : DefaultTargetCxInterop(name, objectFactory, parent),
//    JvmTargetCxInterop {
//    override val target: ForeignTarget
//        get() = when (name) {
//            "macosArm64" -> ForeignTarget.MacosArm64
//            else         -> TODO("unsupported target: $name")
//        }
//
//    class Factory(
//        private val objectFactory: ObjectFactory,
//        private val parent: DefaultJvmPlatformCxInterop
//    ) : NamedDomainObjectFactory<DefaultJvmTargetCxInterop> {
//        override fun create(name: String): DefaultJvmTargetCxInterop {
//            return DefaultJvmTargetCxInterop(name, objectFactory, parent)
//        }
//    }
//}
