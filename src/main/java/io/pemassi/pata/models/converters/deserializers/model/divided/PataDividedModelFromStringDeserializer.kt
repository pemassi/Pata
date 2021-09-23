/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.model.divided

import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.models.DividedPataModel
import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import java.nio.charset.Charset

class PataDividedModelFromStringDeserializer: PataModelDeserializer<String, DividedPataModel> {

    override fun deserialize(
        instance: DividedPataModel,
        input: String,
        charset: Charset?,
        dataFieldDeserializers: PataDataFieldDeserializerMap
    ): DividedPataModel
    {
        val dataList = input.split(instance.delimiters)

        for(i in dataList.indices)
        {
            val (property, _) = instance.propertyDatabase[i]
            val data = dataList[i]
            val deserializer = dataFieldDeserializers.get<String>(property.returnType)
            val inputData = deserializer.deserialize(data, charset ?: instance.modelCharset)
            property.setter.call(instance, inputData)
        }

        return instance
    }

}