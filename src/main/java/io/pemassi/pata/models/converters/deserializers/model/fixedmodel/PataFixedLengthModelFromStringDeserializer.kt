/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.model.fixedmodel

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import java.nio.charset.Charset

class PataFixedLengthModelFromStringDeserializer: PataModelDeserializer<String, FixedLengthPataModel<*>> {

    override fun deserialize(
        instance: FixedLengthPataModel<*>,
        input: String,
        charset: Charset?,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): FixedLengthPataModel<*>
    {
        return PataFixedLengthModelFromByteArrayDeserializer().deserialize(
            instance = instance,
            input = input.toByteArray(charset ?: instance.modelCharset),
            charset = charset,
            dataFieldDeserializers = dataFieldDeserializers
        )
    }
}