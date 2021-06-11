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

    inline fun <reified InputType, reified ModelType: PataModel<*>> get(): PataModelDeserializer<InputType, ModelType>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelDeserializerMap with InputType(${InputType::class.starProjectedType})")

        val dataFieldDeserializer = dataMap[ModelType::class] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${ModelType::class})")

        return dataFieldDeserializer as PataModelDeserializer<InputType, ModelType>
    }
}