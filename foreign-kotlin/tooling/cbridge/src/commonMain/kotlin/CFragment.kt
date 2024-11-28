package dev.whyoleg.foreign.tooling.cbridge

import kotlinx.serialization.*

public typealias CTarget = String

@Serializable
public class CFragment(
    public val variables: List<CVariable>,
    public val unnamedEnumConstants: List<CUnnamedEnumConstant>,
    public val enums: List<CEnum>,
    public val typedefs: List<CTypedef>,
    public val records: List<CRecord>,
    public val functions: List<CFunction>,
)
