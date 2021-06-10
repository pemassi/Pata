/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.interfaces.*
import io.pemassi.pata.models.deserializers.field.PataByteArrayToIntDeserializer
import io.pemassi.pata.models.deserializers.field.PataByteArrayToLongDeserializer
import io.pemassi.pata.models.deserializers.field.PataByteArrayToStringDeserializer
import io.pemassi.pata.models.deserializers.field.PataDataFieldDeserializerMap
import io.pemassi.pata.models.deserializers.model.PataFixedLengthModelByteArrayToStringDeserializer
import io.pemassi.pata.models.deserializers.model.PataFixedLengthModelStringToStringDeserializer
import io.pemassi.pata.models.deserializers.model.PataModelDeserializerMap
import io.pemassi.pata.models.serializers.field.PataDataFieldSerializerMap
import io.pemassi.pata.models.serializers.field.PataIntToStringSerializer
import io.pemassi.pata.models.serializers.field.PataLongToStringSerializer
import io.pemassi.pata.models.serializers.field.PataStringToStringSerializer
import io.pemassi.pata.models.serializers.model.PataFixedLengthModelToByteArraySerializer
import io.pemassi.pata.models.serializers.model.PataFixedLengthModelToStringSerializer
import io.pemassi.pata.models.serializers.model.PataModelSerializerMap
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
            .register(PataFixedLengthModelStringToStringDeserializer())
            .register(PataFixedLengthModelByteArrayToStringDeserializer())

        //Data Serializers
        dataFieldSerializerMap
            .register(PataIntToStringSerializer())
            .register(PataLongToStringSerializer())
            .register(PataStringToStringSerializer())

        //Data Deserializers
        dataFieldDeserializerMap
            .register(PataByteArrayToIntDeserializer())
            .register(PataByteArrayToLongDeserializer())
            .register(PataByteArrayToStringDeserializer())
    }

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> deserialize(input: InputType, overrideCharset: Charset? = null, oldInstance: ModelType? = null): ModelType
    {
        val castedDeserializer = modelDeserializerMap.get<InputType, ModelType, DataType>()

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

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> registerModelDeserializer(newModelDeserializer: PataModelDeserializer<InputType, ModelType, DataType>)
    {
        modelDeserializerMap.register(newModelDeserializer)
    }
}