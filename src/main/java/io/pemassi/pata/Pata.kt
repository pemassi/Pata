/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.interfaces.*
import io.pemassi.pata.models.converters.deserializers.field.*
import io.pemassi.pata.models.converters.deserializers.model.divided.PataDividedModelFromStringDeserializer
import io.pemassi.pata.models.converters.deserializers.model.fixed_length.PataFixedLengthModelFromByteArrayDeserializer
import io.pemassi.pata.models.converters.deserializers.model.fixed_length.PataFixedLengthModelFromStringDeserializer
import io.pemassi.pata.models.converters.serializers.field.*
import io.pemassi.pata.models.converters.serializers.model.divided.PataDividedModelToStringSerializer
import io.pemassi.pata.models.converters.serializers.model.fixed_length.PataFixedLengthModelToByteArraySerializer
import io.pemassi.pata.models.converters.serializers.model.fixed_length.PataFixedLengthModelToStringSerializer
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
        .register(PataDividedModelToStringSerializer())

    //PataModel Deserializers
    val modelDeserializerMap = PataModelDeserializerMap()
        .register(PataFixedLengthModelFromStringDeserializer())
        .register(PataFixedLengthModelFromByteArrayDeserializer())
        .register(PataDividedModelFromStringDeserializer())

    //Data Serializers
    val dataFieldSerializerMap = PataDataFieldSerializerMap()
        .register(PataDataFieldIntToStringSerializer())
        .register(PataDataFieldLongToStringSerializer())
        .register(PataDataFieldStringToStringSerializer())
        .register(PataDataFieldByteArrayToByteArraySerializer())
        .register(PataDataFieldByteArrayToStringSerializer())

    //Data Deserializers
    val dataFieldDeserializerMap = PataDataFieldDeserializerMap()
        .register(PataDataFieldStringToStringDeserializer())
        .register(PataDataFieldStringToByteArrayDeserializer())
        .register(PataDataFieldStringToIntDeserializer())
        .register(PataDataFieldStringToLongDeserializer())
        .register(PataDataFieldByteArrayToIntDeserializer())
        .register(PataDataFieldByteArrayToLongDeserializer())
        .register(PataDataFieldByteArrayToStringDeserializer())
        .register(PataDataFieldByteArrayToByteArrayDeserializer())

    inline fun <reified InputType, reified OutputType: PataModel<*>> deserialize(input: InputType, overrideCharset: Charset? = null, oldInstance: OutputType? = null): OutputType
    {
        val castedDeserializer = modelDeserializerMap.get<InputType, OutputType>()

        val instance = oldInstance ?: OutputType::class.createInstance()

        return castedDeserializer.deserialize(instance, input, overrideCharset, dataFieldDeserializerMap) as OutputType
    }

    inline fun <reified InputType: PataModel<DataType>, reified DataType> serialize(dataModel: InputType, charset: Charset? = null): DataType
    {
        val serializer = modelSerializerMap.get<InputType, DataType>()

        return serializer.serialize(dataModel, charset, dataFieldSerializerMap)
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