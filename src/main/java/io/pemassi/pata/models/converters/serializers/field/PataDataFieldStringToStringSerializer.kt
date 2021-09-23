/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataDataFieldStringToStringSerializer: PataDataFieldSerializer<String, String>
{
    override fun serialize(input: String, charset: Charset): String {
        return input
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEndByByteLength(expectedSize, ' ', charset)
    }
}