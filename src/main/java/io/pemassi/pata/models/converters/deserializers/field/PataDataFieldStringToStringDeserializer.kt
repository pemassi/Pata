/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataDataFieldStringToStringDeserializer: PataDataFieldDeserializer<String, String>
{
    override fun deserialize(data: String, charset: Charset): String {
        return data.trim()
    }
}