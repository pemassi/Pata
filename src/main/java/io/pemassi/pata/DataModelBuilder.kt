/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.interfaces.PataModel
import io.pemassi.pata.models.FixedLengthPataModel
import java.nio.charset.Charset
import java.security.spec.InvalidParameterSpecException
import javax.activation.UnsupportedDataTypeException
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.starProjectedType

class Pata
{
    val dataFieldDeserializerMap = HashMap<KType, PataDataFieldDeserializer<*>>()
    val dataFieldSerializerMap = HashMap<KType, PataDataFieldSerializer<*>>()

    inline fun <reified T: PataModel> deserialize(data: Any, charset: Charset? = null): T
    {
        return when (data)
        {
            is String ->  fromString(data, charset)

            is ByteArray -> fromByteArray(data, charset)

            else -> throw InvalidParameterSpecException("Cannot deserialize data because the data is unsupported type.")
        }
    }

    fun serialize(dataModel: PataModel, charset: Charset?): String
    {

    }

    inline fun <reified T: PataModel> fromString(data: String, charset: Charset? = null): T
    {

    }

    inline fun <reified T: PataModel> fromByteArray(data: ByteArray, charset: Charset?): T
    {
        val instance = T::class.createInstance()

        when(instance)
        {
            is FixedLengthPataModel -> {
                var cursor = 0

                instance.propertyDatabase.forEach {
                    val (property, annotation) = it
                    val startIndex = cursor
                    val endIndex = cursor + annotation.size
                    val splitData = data.copyOfRange(startIndex, endIndex)
                    val serializer = dataFieldDeserializerMap[property.returnType] ?: throw UnsupportedDataTypeException()
                    val inputData = serializer.deserialize(splitData, charset ?: instance.charset)

                    property.setter.call(instance, inputData)

                    cursor += endIndex
                }
            }

            else -> throw InvalidParameterSpecException("Cannot deserialize data because the Pata model is unsupported type.")
        }

        return instance
    }

    inline fun <reified T> registerDataFieldDeserializer(newDataFieldDeserializer: PataDataFieldDeserializer<T>)
    {
        dataFieldDeserializerMap[T::class.starProjectedType] = newDataFieldDeserializer
    }

    inline fun <reified T> registerDataFieldSerializer(newDataFieldSerializer: PataDataFieldSerializer<T>)
    {
        dataFieldSerializerMap[T::class.starProjectedType] = newDataFieldSerializer
    }
}