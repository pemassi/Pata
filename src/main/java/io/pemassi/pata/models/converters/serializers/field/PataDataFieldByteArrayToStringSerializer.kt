/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset
import kotlin.experimental.and

class PataDataFieldByteArrayToStringSerializer: PataDataFieldSerializer<ByteArray, String>
{
    override fun serialize(input: ByteArray, charset: Charset): String {
        return bytesToHex(input)
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEndByByteLength(expectedSize, '0', charset)
    }

    private val hexArray = "0123456789ABCDEF".toCharArray()

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF

            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

}