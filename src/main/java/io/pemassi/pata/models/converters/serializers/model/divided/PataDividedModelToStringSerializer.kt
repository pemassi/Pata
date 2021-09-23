/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.model.divided

import io.pemassi.pata.interfaces.PataModelSerializer
import io.pemassi.pata.models.DividedPataModel
import io.pemassi.pata.models.map.PataDataFieldSerializerMap
import java.nio.charset.Charset
import java.security.InvalidParameterException

class PataDividedModelToStringSerializer: PataModelSerializer<DividedPataModel, String> {

    override fun serialize(model: DividedPataModel, charset: Charset?, dataFieldSerializers: PataDataFieldSerializerMap): String {

        val propertyDatabase = model.propertyDatabase

        val dataList = ArrayList<String>(propertyDatabase.size)

        val targetCharset = charset ?: model.modelCharset

        propertyDatabase.forEach {
            val (property, _) = it

            val variableType = property.returnType

            val dataFieldSerializer = dataFieldSerializers.get<String>(variableType)

            val value = property.getter.call(model)

            if(value.toString().contains(model.delimiters))
                throw InvalidParameterException("One of the data fields has delimiter(${model.delimiters}).")

            val serializedValue = dataFieldSerializer.serializeWithCasting(value, targetCharset)

            dataList.add(serializedValue)
        }

        return dataList.joinToString(model.delimiters)
    }
}