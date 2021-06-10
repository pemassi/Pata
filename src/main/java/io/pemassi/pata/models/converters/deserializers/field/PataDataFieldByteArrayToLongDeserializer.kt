/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataDataFieldByteArrayToLongDeserializer: PataDataFieldDeserializer<ByteArray, Long>
{

    override fun deserialize(data: ByteArray, charset: Charset): Long {
        return String(data, charset).toLong()
    }

}