/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataByteArrayToIntDeserializer: PataDataFieldDeserializer<ByteArray, Int>
{

    override fun deserialize(data: ByteArray, charset: Charset): Int {
        return String(data, charset).toInt()
    }

}