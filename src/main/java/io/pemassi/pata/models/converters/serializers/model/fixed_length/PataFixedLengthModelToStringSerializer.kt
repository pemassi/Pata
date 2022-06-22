/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.serializers.model.fixed_length

import io.pemassi.pata.enums.PaddingMode
import io.pemassi.pata.exceptions.DataFieldNullException
import io.pemassi.pata.exceptions.DataModelSizeExceedException
import io.pemassi.pata.exceptions.DataModelSizeNeedMoreException
import io.pemassi.pata.interfaces.PataModelSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.map.PataDataFieldSerializerMap
import java.nio.charset.Charset
import kotlin.reflect.full.withNullability

class PataFixedLengthModelToStringSerializer: PataModelSerializer<FixedLengthPataModel<String>, String> {

    override fun serialize(model: FixedLengthPataModel<String>, charset: Charset?, dataFieldSerializers: PataDataFieldSerializerMap): String {

        val propertyDatabase = model.propertyDatabase

        val builder = StringBuilder(model.totalLength)

        val targetCharset = charset ?: model.modelCharset

        propertyDatabase.forEach {
            val (property, annotation) = it

            //Field Info
            val name = annotation.name
            val expectedSize = annotation.size
            val variableName = property.name
            val variableType = property.returnType.withNullability(false)

            //Try to get serializer
            val dataFieldSerializer = dataFieldSerializers.get<String>(variableType)

            //Get Field Value
            val value = property.getter.call(model) ?:
                throw DataFieldNullException(property)

            //Try to serialize
            val serializedValue = dataFieldSerializer.serializeWithCasting(value, targetCharset)

            //When exceed length, throw exception.
            val byteArraySerializedValue = serializedValue.toByteArray(targetCharset)
            val actualSize = byteArraySerializedValue.size
            if (actualSize > expectedSize) {
                throw DataModelSizeExceedException(
                    modelName = this::class.simpleName ?: "",
                    dataName = name,
                    variableName = variableName,
                    expectedSize = expectedSize,
                    actualSize = actualSize,
                    data = serializedValue,
                    dataTable = model.toLog()
                )
            }

            //Additional check depend on padding mode.
            when(model.paddingMode)
            {
                PaddingMode.STRICT -> {
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

                PaddingMode.LENIENT -> {
                    //DO NOTHING
                }
            }

            builder.append(dataFieldSerializer.padding(serializedValue, expectedSize, targetCharset))
        }

        return builder.toString()
    }
}