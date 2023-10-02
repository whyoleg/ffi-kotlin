package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.api.interfaces.*
import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.model.*

internal class DefaultForeignExtension(
    objectFactory: ObjectFactory
) : ForeignExtension {
    override val interfaces: ForeignInterfaces = DefaultForeignInterfaces(objectFactory)
}
