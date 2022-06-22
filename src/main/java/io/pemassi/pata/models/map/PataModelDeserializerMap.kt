/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataModel
import io.pemassi.pata.interfaces.PataModelDeserializer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses

class PataModelDeserializerMap
{
    val map = HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>>>>()

    inline fun <reified InputType, reified ModelType: PataModel<*>> register(newModelDeserializer: PataModelDeserializer<InputType, ModelType>): PataModelDeserializerMap
    {
        val dataMap = map.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        dataMap[ModelType::class] = newModelDeserializer

        return this
    }

    inline fun <reified InputType, reified OutputType: PataModel<*>> get(): PataModelDeserializer<InputType, PataModel<*>>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelDeserializerMap with InputType(${InputType::class.starProjectedType})")

        val dataFieldDeserializer = dataMap[OutputType::class]

        if(dataFieldDeserializer != null)
            @Suppress("UNCHECKED_CAST")
            return dataFieldDeserializer as PataModelDeserializer<InputType, PataModel<*>>

        for(clazz in OutputType::class.superclasses)
        {
            if(dataMap[clazz] != null)
                @Suppress("UNCHECKED_CAST")
                return dataMap[clazz] as PataModelDeserializer<InputType, PataModel<*>>
        }

        throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${OutputType::class})")
    }
}