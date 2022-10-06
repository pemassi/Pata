/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import java.nio.charset.Charset
import kotlin.reflect.KProperty

interface PataDataFieldDeserializer<InputType, DataType> {

    @Deprecated("Please replace with new interface.", ReplaceWith("""
        fun deserialize(
            data: InputType,
            charset: Charset,
            replaceNullMode: ReplaceNullMode,
            trimMode: TrimMode,
            checkNullMode: CheckNullMode,
            property: KProperty<InputType>,
        ): DataType?
    """), DeprecationLevel.ERROR)
    fun deserialize(data: InputType?, charset: Charset): DataType
    {
        throw UnsupportedOperationException()
    }

    fun deserialize(
        data: InputType?,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>,
    ): DataType?

}
