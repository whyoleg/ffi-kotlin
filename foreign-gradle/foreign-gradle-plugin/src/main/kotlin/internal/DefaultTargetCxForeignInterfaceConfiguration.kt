package dev.whyoleg.foreign.gradle.internal

//internal sealed class DefaultTargetCxInterop(
//    val targetName: String,
//    objectFactory: ObjectFactory,
//    parent: DefaultPlatformCxInterop
//) : DefaultLeafCxInterop(objectFactory, parent),
//    TargetCxInterop,
//    Named {
//    final override fun getName(): String = targetName
//    abstract val target: ForeignTarget
//
//    private val platformName = parent.platformName
//    private val interfaceName = parent.interfaceName
//
//    private val generateIndexTaskName = "generateCxCompilerIndex${
//        interfaceName.replaceFirstChar(Char::uppercase)
//    }${
//        platformName.replaceFirstChar(Char::uppercase)
//    }${
//        targetName.replaceFirstChar(Char::uppercase)
//    }"
//
//    fun registerGenerateIndexTask(
//        tasks: TaskContainer
//    ) {
//        tasks.register<DefaultGenerateCxCompilerIndexTask>(generateIndexTaskName) {
//            this.target.set(this@DefaultTargetCxInterop.target)
//        }
//
//        tasks.register(generateIndexTaskName) {
//            it.doLast {
//                println(generateIndexTaskName)
//            }
//        }
//        //`generateCxCompilerIndex Libcrypto Jvm MacosArm64`
//        //build/foreign/interfaces/libcrypto/index/jvmMacosArm64.json
////        tasks.register()
//    }
//}
