/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.model

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.deserializers.field.PataDataFieldDeserializerMap
import java.nio.charset.Charset

class PataFixedLengthModelByteArrayToStringDeserializer: PataModelDeserializer<ByteArray, FixedLengthPataModel<String>, String> {

    override fun deserialize(
        instance: FixedLengthPataModel<String>,
        input: ByteArray,
        charset: Charset?,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): FixedLengthPataModel<String>
    {
        var cursor = 0

        instance.propertyDatabase.forEach {
            val (property, annotation) = it
            val startIndex = cursor
            val endIndex = cursor + annotation.size
            val splitData = input.copyOfRange(startIndex, endIndex)
            val deserializer = dataFieldDeserializers.get<ByteArray>(property.returnType)
            val inputData = deserializer.deserialize(splitData, charset ?: instance.modelCharset)

            property.setter.call(instance, inputData)

            cursor = endIndex
        }

        return instance
    }
}