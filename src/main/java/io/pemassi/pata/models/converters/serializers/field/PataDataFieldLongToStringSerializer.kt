/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataDataFieldLongToStringSerializer: PataDataFieldSerializer<Long, String>
{

    override fun serialize(input: Long, charset: Charset): String {
        return input.toString()
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padStart(expectedSize, '0')
    }

}