/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

interface PataDataFieldSerializer<T>: PataDataPadding<T> {

    fun serialize(data: T, charset: Charset): String

    fun serializeWithPadding(data: T, expectedSize: Int, charset: Charset): String
    {
        return padding(serialize(data, charset), expectedSize, charset)
    }

}