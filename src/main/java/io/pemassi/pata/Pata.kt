/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.interfaces.*
import io.pemassi.pata.models.converters.deserializers.field.PataDataFieldByteArrayToByteArrayDeserializer
import io.pemassi.pata.models.converters.deserializers.field.PataDataFieldByteArrayToIntDeserializer
import io.pemassi.pata.models.converters.deserializers.field.PataDataFieldByteArrayToLongDeserializer
import io.pemassi.pata.models.converters.deserializers.field.PataDataFieldByteArrayToStringDeserializer
import io.pemassi.pata.models.converters.deserializers.model.fixedmodel.PataFixedLengthModelFromByteArrayDeserializer
import io.pemassi.pata.models.converters.deserializers.model.fixedmodel.PataFixedLengthModelFromStringDeserializer
import io.pemassi.pata.models.converters.serializers.field.*
import io.pemassi.pata.models.converters.serializers.model.fixedmodel.PataFixedLengthModelToByteArraySerializer
import io.pemassi.pata.models.converters.serializers.model.fixedmodel.PataFixedLengthModelToStringSerializer
import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import io.pemassi.pata.models.map.PataDataFieldSerializerMap
import io.pemassi.pata.models.map.PataModelDeserializerMap
import io.pemassi.pata.models.map.PataModelSerializerMap
import java.nio.charset.Charset
import kotlin.reflect.full.createInstance

class Pata
{
    //PataModel Serializers
    val modelSerializerMap = PataModelSerializerMap()
        .register(PataFixedLengthModelToByteArraySerializer())
        .register(PataFixedLengthModelToStringSerializer())

    //PataModel Deserializers
    val modelDeserializerMap = PataModelDeserializerMap()
        .register(PataFixedLengthModelFromStringDeserializer())
        .register(PataFixedLengthModelFromByteArrayDeserializer())

    //Data Serializers
    val dataFieldSerializerMap = PataDataFieldSerializerMap()
        .register(PataDataFieldIntToStringSerializer())
        .register(PataDataFieldLongToStringSerializer())
        .register(PataDataFieldStringToStringSerializer())
        .register(PataDataFieldByteArrayToByteArraySerializer())
        .register(PataDataFieldByteArrayToStringSerializer())

    //Data Deserializers
    val dataFieldDeserializerMap = PataDataFieldDeserializerMap()
        .register(PataDataFieldByteArrayToIntDeserializer())
        .register(PataDataFieldByteArrayToLongDeserializer())
        .register(PataDataFieldByteArrayToStringDeserializer())
        .register(PataDataFieldByteArrayToByteArrayDeserializer())

    inline fun <reified InputType, reified ModelType: PataModel<*>, reified OutputType: ModelType> deserialize(input: InputType, overrideCharset: Charset? = null, oldInstance: ModelType? = null): OutputType
    {
        val castedDeserializer = modelDeserializerMap.get<InputType, ModelType>()

        val instance = OutputType::class.createInstance()

        return castedDeserializer.deserialize(instance, input, overrideCharset, dataFieldDeserializerMap) as OutputType
    }

    inline fun <reified InputType: ModelType, reified ModelType: PataModel<DataType>, reified DataType> serialize(dataModel: InputType, charset: Charset? = null): DataType
    {
        val serializer = modelSerializerMap.get<ModelType, DataType>()

        return serializer.serialize(dataModel, charset, dataFieldSerializerMap)
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> deserializeFromString(input: String, overrideCharset: Charset? = null, oldInstance: ModelType? = null): ModelType
    {
        return deserialize(input, overrideCharset, oldInstance)
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> deserializeFromByteArray(input: ByteArray, overrideCharset: Charset? = null, oldInstance: ModelType? = null): ModelType
    {
        return deserialize(input, overrideCharset, oldInstance)
    }

    inline fun <reified InputType, reified DataType> registerDataFieldDeserializer(newDataFieldDeserializer: PataDataFieldDeserializer<InputType, DataType>): Pata
    {
        dataFieldDeserializerMap.register(newDataFieldDeserializer)

        return this
    }

    inline fun <reified InputType, reified DataType> registerDataFieldSerializer(newDataFieldSerializer: PataDataFieldSerializer<InputType, DataType>): Pata
    {
        dataFieldSerializerMap.register(newDataFieldSerializer)

        return this
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> registerModelSerializer(newModelSerializer: PataModelSerializer<ModelType, DataType>): Pata
    {
        modelSerializerMap.register(newModelSerializer)

        return this
    }

    inline fun <reified InputType, reified ModelType: PataModel<*>> registerModelDeserializer(newModelDeserializer: PataModelDeserializer<InputType, ModelType>): Pata
    {
        modelDeserializerMap.register(newModelDeserializer)

        return this
    }
}