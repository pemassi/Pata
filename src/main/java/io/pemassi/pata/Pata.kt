/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.interfaces.PataModel
import io.pemassi.pata.interfaces.PataModelSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.deserializers.PataIntDeserializer
import io.pemassi.pata.models.deserializers.PataLongDeserializer
import io.pemassi.pata.models.deserializers.PataStringDeserializer
import io.pemassi.pata.models.serializers.field.PataIntSerializer
import io.pemassi.pata.models.serializers.field.PataLongSerializer
import io.pemassi.pata.models.serializers.field.PataStringSerializer
import io.pemassi.pata.models.serializers.model.FixedLengthPataModelSerializer
import java.nio.charset.Charset
import java.security.InvalidParameterException
import java.security.spec.InvalidParameterSpecException
import javax.activation.UnsupportedDataTypeException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

class Pata
{
    val dataFieldDeserializerMap = HashMap<KType, PataDataFieldDeserializer<*>>()
    val dataFieldSerializerMap = HashMap<KType, PataDataFieldSerializer<*>>()
    val modelSerializerMap = HashMap<KType, PataModelSerializer<out PataModel>>()

    constructor()
    {
        //PataModel Serializers
        modelSerializerMap[FixedLengthPataModel::class.starProjectedType] = FixedLengthPataModelSerializer()

        //Data Serializers
        dataFieldSerializerMap[Int::class.starProjectedType] = PataIntSerializer()
        dataFieldSerializerMap[Long::class.starProjectedType] = PataLongSerializer()
        dataFieldSerializerMap[String::class.starProjectedType] = PataStringSerializer()

        //Data Deserializers
        dataFieldDeserializerMap[Int::class.starProjectedType] = PataIntDeserializer()
        dataFieldDeserializerMap[Long::class.starProjectedType] = PataLongDeserializer()
        dataFieldDeserializerMap[String::class.starProjectedType] = PataStringDeserializer()

    }

    inline fun <reified T: PataModel> deserialize(data: Any, overrideCharset: Charset? = null, oldInstance: T? = null): T
    {
        return when (data)
        {
            is String ->  fromString(
                data = data,
                overrideCharset = overrideCharset,
                oldInstance = oldInstance,
            )

            is ByteArray -> fromByteArray(
                data = data,
                overrideCharset = overrideCharset,
                oldInstance = oldInstance,
            )

            else -> throw InvalidParameterSpecException("Cannot deserialize data because the data is unsupported type.")
        }
    }

    fun serialize(dataModel: PataModel, charset: Charset? = null): String
    {
        val clazz = dataModel::class.supertypes.find { it.isSubtypeOf(PataModel::class.starProjectedType) }  ?: throw InvalidParameterException()
        val serializer = modelSerializerMap[clazz] as? PataModelSerializer<PataModel>
            ?: throw UnsupportedDataTypeException()

        return serializer.serialize(dataModel, charset, dataFieldSerializerMap)
    }

    inline fun <reified T: PataModel> fromString(data: String, overrideCharset: Charset? = null, oldInstance: T? = null): T
    {
        val instance = oldInstance ?: T::class.createInstance()

        return fromByteArray(
            data = data.toByteArray(overrideCharset ?: instance.modelCharset),
            overrideCharset = overrideCharset,
            oldInstance = instance,
        )
    }

    inline fun <reified T: PataModel> fromByteArray(data: ByteArray, overrideCharset: Charset? = null, oldInstance: T? = null): T
    {
        val instance = oldInstance ?: T::class.createInstance()

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
                    val inputData = serializer.deserialize(splitData, overrideCharset ?: instance.modelCharset)

                    property.setter.call(instance, inputData)

                    cursor = endIndex
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

    inline fun <reified T: PataModel> registerModelSerializer(newModelSerializer: PataModelSerializer<T>)
    {
        modelSerializerMap[T::class.starProjectedType] = newModelSerializer
    }

    companion object
    {
        private val propertiesCacheMap =
            HashMap<KClass<out PataModel>, List<Pair<KMutableProperty<*>, FixedDataField>>>()
    }
}