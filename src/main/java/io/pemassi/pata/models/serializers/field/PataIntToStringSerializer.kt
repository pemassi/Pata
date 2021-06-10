/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataIntToStringSerializer: PataDataFieldSerializer<Int, String>
{

    override fun serialize(input: Int, charset: Charset): String {
        return input.toString()
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padStart(expectedSize, '0')
    }

}