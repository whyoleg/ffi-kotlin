package dev.whyoleg.foreign.gradle

import dev.whyoleg.foreign.gradle.api.*
import dev.whyoleg.foreign.gradle.api.interfaces.*
import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.model.*
import javax.inject.*

public abstract class DefaultForeignExtension @Inject constructor(
    objectFactory: ObjectFactory
) : ForeignExtension {
    public override val interfaces: ForeignInterfaces = DefaultForeignInterfaces(objectFactory)
}
