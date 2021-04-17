/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataStringDeserializer: PataDataFieldDeserializer<String>
{

    override fun deserialize(data: ByteArray, charset: Charset): String {
        return String(data, charset).trimEnd()
    }

}