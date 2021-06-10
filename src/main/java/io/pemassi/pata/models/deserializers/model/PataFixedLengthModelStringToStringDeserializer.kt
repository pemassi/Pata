/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.model

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.deserializers.field.PataDataFieldDeserializerMap
import java.nio.charset.Charset

class PataFixedLengthModelStringToStringDeserializer: PataModelDeserializer<String, FixedLengthPataModel<String>, String> {

    override fun deserialize(
        instance: FixedLengthPataModel<String>,
        input: String,
        charset: Charset?,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): FixedLengthPataModel<String>
    {
        return PataFixedLengthModelByteArrayToStringDeserializer().deserialize(
            instance = instance,
            input = input.toByteArray(charset ?: instance.modelCharset),
            charset = charset,
            dataFieldDeserializers = dataFieldDeserializers
        )
    }
}