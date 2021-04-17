/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataLongSerializer: PataDataFieldSerializer<Long>
{

    override fun serialize(data: Long, charset: Charset): String {
        return data.toString()
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEnd(expectedSize, '0')
    }

}