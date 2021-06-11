/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class PataDataFieldSerializerMap
{
    val map = HashMap<KType, HashMap<KType, PataDataFieldSerializer<*, *>>>()

    inline fun <reified InputType, reified  DataType> register(dataFieldSerializer: PataDataFieldSerializer<InputType, DataType>): PataDataFieldSerializerMap
    {
        val dataMap = map.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        dataMap[DataType::class.starProjectedType] = dataFieldSerializer

        return this
    }

    inline fun <reified DataType> get(inputType: KType): PataDataFieldSerializer<*, DataType>
    {
        val dataMap = map[inputType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldSerializerMap with InputType(${inputType} -> ${DataType::class.starProjectedType})")

        val dataFieldSerializer = dataMap[DataType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldSerializerMap with DataType(${inputType} -> ${DataType::class.starProjectedType})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldSerializer as PataDataFieldSerializer<*, DataType>
    }

}