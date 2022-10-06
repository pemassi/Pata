/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses

class PataDataFieldDeserializerMap
{
    val map = HashMap<KType, HashMap<KType, PataDataFieldDeserializer<*, *>>>()

    inline fun <reified InputType, reified DataType> register(dataFieldDeserializer: PataDataFieldDeserializer<InputType, DataType>): PataDataFieldDeserializerMap
    {
        val dataMap = map.getOrPut(InputType::class.starProjectedType) {
            HashMap()
        }

        dataMap[DataType::class.starProjectedType] = dataFieldDeserializer

        return this
    }

    inline fun <reified InputType> get(inputProperty: KProperty<*>): PataDataFieldDeserializer<InputType, *>
    {
        val inputClass = inputProperty.returnType.classifier as KClass<*>
        val inputType = inputClass.starProjectedType

        try
        {
            return get(inputType)
        }
        catch(e: DataModelUnsupportedTypeException)
        {
            PataDataFieldSerializerMap.logger.debug("Fail to find deserializer with return type. Try with super classes", e)

            // If not found, try all super classes
            val superClasses = inputClass.superclasses.map { it.starProjectedType }
            superClasses.forEach {
                try
                {
                    return get(it)
                }
                catch(e: DataModelUnsupportedTypeException)
                {
                    // Do nothing
                }
            }
        }

        throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with InputType(${inputType} -> ${InputType::class.starProjectedType}) and also super classes.")
    }

    inline fun <reified InputType> get(dataType: KType): PataDataFieldDeserializer<InputType, *>
    {
        val dataMap = map[InputType::class.starProjectedType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with InputType(${InputType::class.starProjectedType} -> ${dataType})")

        val dataFieldDeserializer = dataMap[dataType] ?:
            throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldDeserializerMap with DataType(${InputType::class.starProjectedType} -> ${dataType})")

        @Suppress("UNCHECKED_CAST")
        return dataFieldDeserializer as PataDataFieldDeserializer<InputType, *>
    }

}
