/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.interfaces.*
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.deserializers.field.PataIntDeserializer
import io.pemassi.pata.models.deserializers.field.PataLongDeserializer
import io.pemassi.pata.models.deserializers.field.PataStringDeserializer
import io.pemassi.pata.models.deserializers.model.StringFixedLengthPataModelFromByteArrayDeserializer
import io.pemassi.pata.models.deserializers.model.StringFixedLengthPataModelFromStringDeserializer
import io.pemassi.pata.models.serializers.field.PataIntSerializer
import io.pemassi.pata.models.serializers.field.PataLongSerializer
import io.pemassi.pata.models.serializers.field.PataStringSerializer
import io.pemassi.pata.models.serializers.model.StringFixedLengthPataModelSerializer
import java.nio.charset.Charset
import java.security.InvalidParameterException
import javax.activation.UnsupportedDataTypeException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.*

class Pata
{
    /**
     * HashMap<DataType, HashMap<PataModelType, PataModelSerializer>
     */
    val modelSerializerMap = HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelSerializer<out PataModel<*>, *>>>()

    /**
     * HashMap<InputType, HashMap<DataType, HashMap<PdataModelType, PataModelSerializer>>>
     */
    val modelDeserializerMap = HashMap<KType, HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>, *>>>>()

    /**
     * HashMap<InputType, HashMap<DataType, PataDataFieldDeserializer>>
     */
    val dataFieldDeserializerMap = HashMap<KType, HashMap<KType, PataDataFieldDeserializer<*, *>>>()

    /**
     * HashMap<InputType, HashMap<DataType, PataDataFieldSerializer>>
     */
    val dataFieldSerializerMap = HashMap<KType, HashMap<KType, PataDataFieldSerializer<*, *>>>()

    constructor()
    {
        //PataModel Serializers
        modelSerializerMap[String::class.starProjectedType] = HashMap<KClass<out PataModel<*>>, PataModelSerializer<out PataModel<*>, *>>().also {
            it[FixedLengthPataModel::class] = StringFixedLengthPataModelSerializer()
        }

        //PataModel Deserializers
        modelDeserializerMap[String::class.starProjectedType] = HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>, *>>>().also {
            it[String::class.starProjectedType] = HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>, *>>().also { it2 ->
                it2[FixedLengthPataModel::class] = StringFixedLengthPataModelFromStringDeserializer()
            }
            it[ByteArray::class.starProjectedType] = HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>, *>>().also { it2 ->
                it2[FixedLengthPataModel::class] = StringFixedLengthPataModelFromByteArrayDeserializer()
            }
        }

        //Data Serializers
        dataFieldSerializerMap[Int::class.starProjectedType] = HashMap<KType, PataDataFieldSerializer<*, *>>().also {
            it[String::class.starProjectedType] = PataIntSerializer()
        }
        dataFieldSerializerMap[Long::class.starProjectedType] = HashMap<KType, PataDataFieldSerializer<*, *>>().also {
            it[String::class.starProjectedType] = PataLongSerializer()
        }
        dataFieldSerializerMap[String::class.starProjectedType] = HashMap<KType, PataDataFieldSerializer<*, *>>().also {
            it[String::class.starProjectedType] = PataStringSerializer()
        }

        //Data Deserializers
        dataFieldDeserializerMap[ByteArray::class.starProjectedType] = HashMap<KType, PataDataFieldDeserializer<*, *>>().also {
            it[Int::class.starProjectedType] = PataIntDeserializer()
        }
        dataFieldDeserializerMap[ByteArray::class.starProjectedType] = HashMap<KType, PataDataFieldDeserializer<*, *>>().also {
            it[Long::class.starProjectedType] = PataLongDeserializer()
        }
        dataFieldDeserializerMap[ByteArray::class.starProjectedType] = HashMap<KType, PataDataFieldDeserializer<*, *>>().also {
            it[String::class.starProjectedType] = PataStringDeserializer()
        }

    }

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> deserialize(input: InputType, overrideCharset: Charset? = null, oldInstance: ModelType? = null): ModelType
    {
        val dataMap = modelDeserializerMap[InputType::class.starProjectedType]
            ?: throw UnsupportedDataTypeException("Cannot find from deserializer map with InputType(${InputType::class})")

        val modelMap = dataMap[DataType::class.starProjectedType]
            ?: throw UnsupportedDataTypeException("Cannot find from deserializer map with DataType(${DataType::class})")

        val pataModelType = ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) }  ?: throw InvalidParameterException()

        val deserializer = modelMap[pataModelType]
            ?: throw UnsupportedDataTypeException("Cannot find from deserializer map with ModelType(${pataModelType})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedDeserializer = deserializer as PataModelDeserializer<InputType, ModelType, DataType>

        return castedDeserializer.deserialize(input, overrideCharset, dataFieldDeserializerMap)
    }

    inline fun <reified DataType> serialize(dataModel: PataModel<DataType>, charset: Charset? = null): DataType
    {
        val modelType = dataModel::class.superclasses.find { it.isSubclassOf(PataModel::class) }  ?: throw InvalidParameterException()

        val modelMap = modelSerializerMap[DataType::class.starProjectedType]
            ?: throw UnsupportedDataTypeException("Cannot find from serializer map with DataType(${DataType::class})")

        val serializer = modelMap[modelType]
            ?: throw UnsupportedDataTypeException("Cannot find from serializer map with dataModel(${modelType})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedSerializer = serializer as PataModelSerializer<PataModel<DataType>, DataType>

        return serializer.serialize(dataModel, charset, dataFieldSerializerMap)
    }

    inline fun <reified T> registerDataFieldDeserializer(newDataFieldDeserializer: PataDataFieldDeserializer<T>)
    {
        dataFieldDeserializerMap[T::class.starProjectedType] = newDataFieldDeserializer
    }

    inline fun <reified InputType, DataType> registerDataFieldSerializer(newDataFieldSerializer: PataDataFieldSerializer<InputType, DataType>)
    {
        dataFieldSerializerMap[T::class.starProjectedType] = newDataFieldSerializer
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> registerModelSerializer(newModelSerializer: PataModelSerializer<ModelType, DataType>)
    {
        val modelMap = modelSerializerMap.getOrPut(DataType::class.starProjectedType) {
            HashMap()
        }

        modelMap[ModelType::class] = newModelSerializer
    }

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> registerModelDeserializer(newModelDeserializer: PataModelDeserializer<InputType, ModelType, DataType>)
    {
        val dataMap = modelDeserializerMap.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        val modelMap = dataMap.getOrPut(DataType::class.starProjectedType) {
            HashMap()
        }

        modelMap[ModelType::class] = newModelDeserializer
    }

    private inline fun <reified T> createType(): KType
    {
        return T::class.starProjectedType
    }

    companion object
    {
        private val propertiesCacheMap =
            HashMap<KClass<out PataModel<*>>, List<Pair<KMutableProperty<*>, FixedDataField>>>()
    }
}