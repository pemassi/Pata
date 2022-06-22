/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

interface PataDataFieldSerializer<InputType: Any?, DataType>: PataDataPadding<DataType> {

    fun serializeWithCasting(input: Any?, charset: Charset): DataType
    {
        @Suppress("UNCHECKED_CAST")
        return serialize(input as InputType, charset)
    }

    fun serialize(input: InputType, charset: Charset): DataType

    fun serializeWithPadding(input: InputType, expectedSize: Int, charset: Charset): DataType
    {
        return padding(serialize(input, charset), expectedSize, charset)
    }

}