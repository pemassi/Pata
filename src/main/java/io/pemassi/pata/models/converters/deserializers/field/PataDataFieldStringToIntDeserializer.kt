/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import java.nio.charset.Charset

class PataDataFieldStringToIntDeserializer: PataDataFieldDeserializer<String, Int>
{
    override fun deserialize(data: String, charset: Charset): Int {
        return data.trim().toInt()
    }
}