/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataModel
import io.pemassi.pata.interfaces.PataModelSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses

class PataModelSerializerMap
{
    val map = HashMap<KType, HashMap<KClass<out PataModel<*>>, PataModelSerializer<out PataModel<*>, *>>>()

    inline fun < reified ModelType: PataModel<DataType>, reified DataType> register(newModelSerializer: PataModelSerializer<ModelType, DataType>): PataModelSerializerMap
    {
        val modelMap = map.getOrPut(DataType::class.starProjectedType) {
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

        modelMap[castedPataModelKClass] = newModelSerializer

        return this
    }

    inline fun <reified ModelType: PataModel<DataType>, reified DataType> get(): PataModelSerializer<ModelType, DataType>
    {
        val modelMap = map[DataType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelSerializerMap with DataType(${DataType::class.starProjectedType})")

        val pataModelKClass = ModelType::class.superclasses.find { it.isSubclassOf(PataModel::class) } ?:
            throw DataModelUnsupportedTypeException("Cannot find PataModel subclass with DataType(${ModelType::class})")

        val dataFieldSerializer = modelMap[pataModelKClass] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelSerializerMap with DataType(${pataModelKClass})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldSerializer as PataModelSerializer<ModelType, DataType>
    }

}