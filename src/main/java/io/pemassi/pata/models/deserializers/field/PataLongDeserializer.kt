/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataLongDeserializer: PataDataFieldDeserializer<ByteArray, Long>
{

    override fun deserialize(data: ByteArray, charset: Charset): Long {
        return String(data, charset).toLong()
    }

}