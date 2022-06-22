/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.kotlin.extensions.common.encodeHexString
import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset
import kotlin.experimental.and

class PataDataFieldByteArrayToStringSerializer: PataDataFieldSerializer<ByteArray, String>
{
    override fun serialize(input: ByteArray, charset: Charset): String {
        return input.encodeHexString()
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEndByByteLength(expectedSize, '0', charset)
    }
}