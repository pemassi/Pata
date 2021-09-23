/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.model.fixed_length

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import java.nio.charset.Charset

class PataFixedLengthModelFromByteArrayDeserializer: PataModelDeserializer<ByteArray, FixedLengthPataModel<*>> {

    override fun deserialize(
        instance: FixedLengthPataModel<*>,
        input: ByteArray,
        charset: Charset?,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): FixedLengthPataModel<*>
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