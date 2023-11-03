package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.api.cx.*
import dev.whyoleg.foreign.gradle.cx.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*

internal sealed class DefaultTargetCxForeignInterfaceConfiguration(
    val targetName: String,
    objectFactory: ObjectFactory,
    parent: DefaultPlatformCxForeignInterfaceConfiguration
) : DefaultLeafCxForeignInterfaceConfiguration(objectFactory, parent),
    TargetCxForeignInterfaceConfiguration,
    Named {
    final override fun getName(): String = targetName
    abstract val target: ForeignTarget

    private val platformName = parent.platformName
    private val interfaceName = parent.interfaceName

    private val generateIndexTaskName = "generateCxCompilerIndex${
        interfaceName.replaceFirstChar(Char::uppercase)
    }${
        platformName.replaceFirstChar(Char::uppercase)
    }${
        targetName.replaceFirstChar(Char::uppercase)
    }"

    fun registerGenerateIndexTask(
        tasks: TaskContainer
    ) {
        tasks.register<DefaultGenerateCxCompilerIndexTask>(generateIndexTaskName) {
            this.target.set(this@DefaultTargetCxForeignInterfaceConfiguration.target)
        }

        tasks.register(generateIndexTaskName) {
            it.doLast {
                println(generateIndexTaskName)
            }
        }
        //`generateCxCompilerIndex Libcrypto Jvm MacosArm64`
        //build/foreign/interfaces/libcrypto/index/jvmMacosArm64.json
//        tasks.register()
    }
}
