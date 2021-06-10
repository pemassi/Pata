/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.model

import io.pemassi.pata.interfaces.*
import io.pemassi.pata.models.FixedLengthPataModel
import java.nio.charset.Charset
import javax.activation.UnsupportedDataTypeException
import kotlin.reflect.KClass

class StringFixedLengthPataModelFromByteArrayDeserializer: PataModelDeserializer<ByteArray, FixedLengthPataModel<String>, String> {

    override fun deserialize(
        input: ByteArray,
        charset: Charset?,
        dataFieldDeserializers: HashMap<KClass<*>, HashMap<KClass<*>, PataDataFieldDeserializer<*, *>>>
    ): FixedLengthPataModel<String> {

        val instance = createInstance()

        var cursor = 0

        instance.propertyDatabase.forEach {
            val (property, annotation) = it
            val startIndex = cursor
            val endIndex = cursor + annotation.size
            val splitData = input.copyOfRange(startIndex, endIndex)
            val serializer = dataFieldDeserializers[property.returnType] ?: throw UnsupportedDataTypeException()
            val inputData = serializer.deserialize(splitData, charset ?: instance.modelCharset)

            property.setter.call(instance, inputData)

            cursor = endIndex
        }

        return instance
    }
}