/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.model.fixed_length

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import java.nio.charset.Charset
import kotlin.reflect.full.withNullability

class PataFixedLengthModelFromStringDeserializer: PataModelDeserializer<String, FixedLengthPataModel<*>> {

    override fun deserialize(
        instance: FixedLengthPataModel<*>,
        input: String,
        charset: Charset,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): FixedLengthPataModel<*>
    {

        var cursor = 0
        val byteArrayInput = input.toByteArray(charset)

        instance.propertyDatabase.forEach {
            val (property, annotation) = it
            val startIndex = cursor
            val endIndex = cursor + annotation.size
            val splitData = byteArrayInput.copyOfRange(startIndex, endIndex)
            val deserializer = dataFieldDeserializers.get<String>(property)
            val inputData = deserializer.deserialize(
                data = String(splitData, charset),
                charset = charset,
                replaceNullMode = instance.replaceNullMode,
                trimMode = instance.trimMode,
                checkNullMode = instance.checkNullMode,
                property = property,
            )
            property.setter.call(instance, inputData)

            cursor = endIndex
        }

        return instance
    }
}
