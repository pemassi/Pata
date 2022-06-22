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

        modelMap[ModelType::class] = newModelSerializer

        return this
    }

    inline fun <reified InputType: PataModel<DataType>, reified DataType> get(): PataModelSerializer<PataModel<DataType>, DataType>
    {
        val modelMap = map[DataType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataModelSerializerMap with DataType(${DataType::class.starProjectedType})")

        modelMap[InputType::class]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as PataModelSerializer<PataModel<DataType>, DataType>
        }

        for(clazz in InputType::class.superclasses)
        {
            modelMap[clazz]?.let {
                @Suppress("UNCHECKED_CAST")
                return it as PataModelSerializer<PataModel<DataType>, DataType>
            }
        }

        throw DataModelUnsupportedTypeException("Cannot find from PataModelSerializerMap with DataType(${InputType::class})")
    }
}