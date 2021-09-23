/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.model.fixed_length

import io.pemassi.pata.enums.PaddingMode
import io.pemassi.pata.exceptions.DataModelSizeExceedException
import io.pemassi.pata.exceptions.DataModelSizeNeedMoreException
import io.pemassi.pata.interfaces.PataModelSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.map.PataDataFieldSerializerMap
import java.nio.charset.Charset

class PataFixedLengthModelToStringSerializer: PataModelSerializer<FixedLengthPataModel<String>, String> {

    override fun serialize(model: FixedLengthPataModel<String>, charset: Charset?, dataFieldSerializers: PataDataFieldSerializerMap): String {

        val propertyDatabase = model.propertyDatabase

        val builder = StringBuilder()

        val targetCharset = charset ?: model.modelCharset

        propertyDatabase.forEach {
            val (property, annotation) = it

            val name = annotation.name
            val expectedSize = annotation.size
            val variableName = property.name
            val variableType = property.returnType

            //Need to find better way to check code errors in compile level.
            //There is no logic error because we are checking with type when getting serializer.
            val dataFieldSerializer = dataFieldSerializers.get<String>(variableType)

            val value = property.getter.call(model)
            val serializedValue = dataFieldSerializer.serializeWithCasting(value, targetCharset)
            val byteArraySerializedValue = serializedValue.toByteArray(targetCharset)
            val actualSize = byteArraySerializedValue.size

            if (actualSize > expectedSize)
                throw DataModelSizeExceedException(
                    modelName = this::class.simpleName ?: "",
                    dataName = name,
                    variableName = variableName,
                    expectedSize = expectedSize,
                    actualSize = actualSize,
                    data = serializedValue,
                    dataTable = model.toLog()
                )

            if(model.paddingMode == PaddingMode.STRICT)
            {
                if(actualSize != expectedSize)
                    throw DataModelSizeNeedMoreException(
                        modelName = this::class.simpleName ?: "",
                        dataName = name,
                        variableName = variableName,
                        expectedSize = expectedSize,
                        actualSize = actualSize,
                        data = serializedValue,
                        dataTable = model.toLog()
                    )
            }

            builder.append(dataFieldSerializer.padding(serializedValue, expectedSize, targetCharset))
        }

        return builder.toString()
    }
}