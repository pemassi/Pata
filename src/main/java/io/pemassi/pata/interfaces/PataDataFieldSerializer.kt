/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

interface PataDataFieldSerializer<InputType, DataType>: PataDataPadding<DataType> {

    fun serialize(input: InputType, charset: Charset): DataType

    fun serializeWithPadding(input: InputType, expectedSize: Int, charset: Charset): DataType
    {
        return padding(serialize(input, charset), expectedSize, charset)
    }

}