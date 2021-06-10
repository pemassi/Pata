/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataDataFieldByteArrayToStringDeserializer: PataDataFieldDeserializer<ByteArray, String>
{

    override fun deserialize(data: ByteArray, charset: Charset): String {
        return String(data, charset).trimEnd()
    }

}