/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataDataFieldByteArrayToByteArraySerializer: PataDataFieldSerializer<ByteArray, ByteArray>
{
    override fun serialize(input: ByteArray, charset: Charset): ByteArray {
        return input
    }

    override fun padding(data: ByteArray, expectedSize: Int, charset: Charset): ByteArray {
        if(data.size == expectedSize)
            return data

        val temp = ByteArray(expectedSize)
        System.arraycopy(data, 0, temp, 0, data.size)

        return temp
    }
}