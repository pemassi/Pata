/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.map

import io.pemassi.kotlin.extensions.slf4j.getLogger
import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.withNullability

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

    inline fun <reified DataType> get(inputProperty: KProperty<*>): PataDataFieldSerializer<*, DataType>
    {
        val inputClass = inputProperty.returnType.classifier as KClass<*>
        val inputType = inputClass.starProjectedType

        try
        {
            return get(inputType)
        }
        catch(e: DataModelUnsupportedTypeException)
        {
            logger.debug("Fail to find serializer with return type. Try with super classes", e)

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

        throw DataModelUnsupportedTypeException("Cannot find from PataDataFieldSerializerMap with InputType(${inputType} -> ${DataType::class.starProjectedType}) and also super classes.")
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


    companion object
    {
        val logger by getLogger()
    }
}
