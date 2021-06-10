/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataLongSerializer: PataDataFieldSerializer<Long, String>
{

    override fun serialize(input: Long, charset: Charset): String {
        return input.toString()
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEnd(expectedSize, '0')
    }

}