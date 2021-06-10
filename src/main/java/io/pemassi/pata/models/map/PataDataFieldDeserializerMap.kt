/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

class PataDataFieldDeserializerMap
{
    val map = HashMap<KType, HashMap<KType, PataDataFieldDeserializer<*, *>>>()

    inline fun <reified InputType, reified  DataType> register(dataFieldDeserializer: PataDataFieldDeserializer<InputType, DataType>): PataDataFieldDeserializerMap
    {
        val dataMap = map.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        dataMap[DataType::class.starProjectedType] = dataFieldDeserializer

        return this
    }

    inline fun <reified InputType> get(dataType: KType): PataDataFieldDeserializer<InputType, *>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with InputType(${InputType::class.starProjectedType})")

        val dataFieldDeserializer = dataMap[dataType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${dataType})")

        //Need to find better way to check code errors in compile level.
        //There is no logic error because we are checking with type when getting it.
        return dataFieldDeserializer as PataDataFieldDeserializer<InputType, *>
    }

}