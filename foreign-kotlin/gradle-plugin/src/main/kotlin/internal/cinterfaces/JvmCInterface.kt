package dev.whyoleg.foreign.gradle.internal.cinterfaces

//internal class DefaultJvmCInterfaceOptions(
//    private val objects: ObjectFactory
//) : DefaultPlatformCInterfaceOptions(objects), ForeignJvmCInterfaceTarget {
//    override val foreignPlatform: ForeignPlatform get() = ForeignPlatform.Jvm
//
//    override val runtimeKind: Property<ForeignJvmRuntimeKind> =
//        objects.property(ForeignJvmRuntimeKind::class.java).convention(ForeignJvmRuntimeKind.BOTH)
//
//    override val targets: NamedDomainObjectContainer<DefaultJvmTargetCInterfaceOptions> =
//        objects.domainObjectContainer(DefaultJvmTargetCInterfaceOptions::class.java)
//
//    override fun macosArm64(configure: ForeignJvmTargetCInterfaceOptions.() -> Unit) {
//        target(ForeignTarget.MacosArm64).configure()
//    }
//
//    override fun macosX64(configure: ForeignJvmTargetCInterfaceOptions.() -> Unit) {
//        target(ForeignTarget.MacosX64).configure()
//    }
//
//    override fun linuxX64(configure: ForeignJvmTargetCInterfaceOptions.() -> Unit) {
//        target(ForeignTarget.LinuxX64).configure()
//    }
//
//    override fun mingwX64(configure: ForeignJvmTargetCInterfaceOptions.() -> Unit) {
//        target(ForeignTarget.MingwX64).configure()
//    }
//
//    private fun target(target: ForeignTarget.JvmCompatible): ForeignJvmTargetCInterfaceOptions {
//        return targets.getOrCreate(target.name) {
//            DefaultJvmTargetCInterfaceOptions(objects, target).withAllFrom(this)
//        }
//    }
//}
//
//internal class DefaultJvmTargetCInterfaceOptions(
//    objects: ObjectFactory,
//    override val foreignTarget: ForeignTarget.JvmCompatible
//) : DefaultTargetCInterfaceOptions(objects), ForeignJvmTargetCInterfaceOptions
