/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.field

import io.pemassi.pata.interfaces.PataDataFieldSerializer
import javax.activation.UnsupportedDataTypeException
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
            throw UnsupportedDataTypeException("Cannot find from PataDataFieldSerializerMap with InputType(${inputType})")

        val dataFieldSerializer = dataMap[DataType::class.starProjectedType] ?:
            throw UnsupportedDataTypeException("Cannot find from PataDataFieldSerializerMap with DataType(${DataType::class.starProjectedType})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldSerializer as PataDataFieldSerializer<*, DataType>
    }

}