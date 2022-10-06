/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataDataFieldEnumToStringSerializer: PataDataFieldSerializer<Enum<*>, String>
{
    override fun serialize(input: Enum<*>, charset: Charset): String {
        return input.name
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEndByByteLength(expectedSize, ' ', charset)
    }
}
