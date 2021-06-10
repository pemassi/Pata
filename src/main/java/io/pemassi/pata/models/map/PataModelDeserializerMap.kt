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
import kotlin.reflect.full.isSubclassOf
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

        val pataModelKClass = if(ModelType::class.isSubclassOf(PataModel::class))
            ModelType::class
        else
            ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) } ?:
                throw DataModelUnsupportedTypeException("Cannot find PataModel subclass with DataType(${ModelType::class})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedPataModelKClass = pataModelKClass as KClass<out PataModel<*>>

        dataMap[castedPataModelKClass] = newModelDeserializer

        return this
    }

    inline fun <reified InputType, reified ModelType: PataModel<*>> get(): PataModelDeserializer<InputType, ModelType>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelDeserializerMap with InputType(${InputType::class.starProjectedType})")

        val pataModelKClass = ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) } ?:
            throw DataModelUnsupportedTypeException("Cannot find PataModel subclass with DataType(${ModelType::class})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedPataModelKClass = pataModelKClass as KClass<out PataModel<*>>

        val dataFieldDeserializer = dataMap[castedPataModelKClass] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${pataModelKClass})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldDeserializer as PataModelDeserializer<InputType, ModelType>
    }

}