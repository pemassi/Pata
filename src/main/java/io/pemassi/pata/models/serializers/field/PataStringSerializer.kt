/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.field

import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import java.nio.charset.Charset

class PataStringSerializer: PataDataFieldSerializer<String>
{

    override fun serialize(data: String, charset: Charset): String {
        return data
    }

    override fun padding(data: String, expectedSize: Int, charset: Charset): String {
        return data.padEndByByteLength(expectedSize, ' ', charset)
    }
}