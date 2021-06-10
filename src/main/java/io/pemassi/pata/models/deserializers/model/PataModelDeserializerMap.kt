/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.model

import io.pemassi.pata.interfaces.PataModel
import io.pemassi.pata.interfaces.PataModelDeserializer
import javax.activation.UnsupportedDataTypeException
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses

class PataModelDeserializerMap
{
    val map = HashMap<KType, HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelDeserializer<*, out PataModel<*>, *>>>>()

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> register(newModelDeserializer: PataModelDeserializer<InputType, ModelType, DataType>): PataModelDeserializerMap
    {
        val dataMap = map.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        val modelMap = dataMap.getOrPut(DataType::class.starProjectedType) {
            HashMap()
        }


        val pataModelKClass = if(ModelType::class.isSubclassOf(PataModel::class))
            ModelType::class
        else
            ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) } ?:
                throw UnsupportedDataTypeException("Cannot find PataModel subclass with DataType(${ModelType::class})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedPataModelKClass = pataModelKClass as KClass<out PataModel<*>>

        modelMap[castedPataModelKClass] = newModelDeserializer

        return this
    }

    inline fun <reified InputType, reified ModelType: PataModel<DataType>, reified DataType> get(): PataModelDeserializer<InputType, ModelType, DataType>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw UnsupportedDataTypeException("Cannot find from PataModelDeserializerMap with InputType(${InputType::class.starProjectedType})")

        val modelMap = dataMap[DataType::class.starProjectedType] ?:
            throw UnsupportedDataTypeException("Cannot find from PataModelDeserializerMap with DataType(${DataType::class.starProjectedType})")

        val pataModelKClass = ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) } ?:
            throw UnsupportedDataTypeException("Cannot find PataModel subclass with DataType(${ModelType::class})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        val castedPataModelKClass = pataModelKClass as KClass<out PataModel<*>>

        val dataFieldDeserializer = modelMap[castedPataModelKClass] ?:
            throw UnsupportedDataTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${pataModelKClass})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldDeserializer as PataModelDeserializer<InputType, ModelType, DataType>
    }

}