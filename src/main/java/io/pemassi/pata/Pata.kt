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
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldByteArrayToByteArraySerializer
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldIntToStringSerializer
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldLongToStringSerializer
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldStringToStringSerializer
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
    val modelSerializerMap = PataModelSerializerMap()
    val modelDeserializerMap = PataModelDeserializerMap()
    val dataFieldDeserializerMap = PataDataFieldDeserializerMap()
    val dataFieldSerializerMap = PataDataFieldSerializerMap()

    constructor()
    {
        //PataModel Serializers
        modelSerializerMap
            .register(PataFixedLengthModelToByteArraySerializer())
            .register(PataFixedLengthModelToStringSerializer())

        //PataModel Deserializers
        modelDeserializerMap
            .register(PataFixedLengthModelFromStringDeserializer())
            .register(PataFixedLengthModelFromByteArrayDeserializer())

        //Data Serializers
        dataFieldSerializerMap
            .register(PataDataFieldIntToStringSerializer())
            .register(PataDataFieldLongToStringSerializer())
            .register(PataDataFieldStringToStringSerializer())
            .register(PataDataFieldByteArrayToByteArraySerializer())

        //Data Deserializers
        dataFieldDeserializerMap
            .register(PataDataFieldByteArrayToIntDeserializer())
            .register(PataDataFieldByteArrayToLongDeserializer())
            .register(PataDataFieldByteArrayToStringDeserializer())
            .register(PataDataFieldByteArrayToByteArrayDeserializer())
    }

    inline fun <reified InputType, reified ModelType: PataModel<*>> deserialize(input: InputType, overrideCharset: Charset? = null, oldInstance: ModelType? = null): ModelType
    {
        val castedDeserializer = modelDeserializerMap.get<InputType, ModelType>()

        val instance = ModelType::class.createInstance()

        return castedDeserializer.deserialize(instance, input, overrideCharset, dataFieldDeserializerMap)
    }

    inline fun <reified ModelType: PataModel<DataType>,reified DataType> serialize(dataModel: ModelType, charset: Charset? = null): DataType
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

    inline fun <reified InputType, reified DataType> registerDataFieldDeserializer(newDataFieldDeserializer: PataDataFieldDeserializer<InputType, DataType>)
    {
        dataFieldDeserializerMap.register(newDataFieldDeserializer)
    }

    inline fun <reified InputType, reified DataType> registerDataFieldSerializer(newDataFieldSerializer: PataDataFieldSerializer<InputType, DataType>)
    {
        dataFieldSerializerMap.register(newDataFieldSerializer)
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> registerModelSerializer(newModelSerializer: PataModelSerializer<ModelType, DataType>)
    {
        modelSerializerMap.register(newModelSerializer)
    }

    inline fun <reified InputType, reified ModelType: PataModel<*>> registerModelDeserializer(newModelDeserializer: PataModelDeserializer<InputType, ModelType>)
    {
        modelDeserializerMap.register(newModelDeserializer)
    }
}