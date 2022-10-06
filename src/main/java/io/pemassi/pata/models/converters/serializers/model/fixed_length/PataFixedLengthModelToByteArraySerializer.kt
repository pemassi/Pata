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

class PataFixedLengthModelToByteArraySerializer: PataModelSerializer<FixedLengthPataModel<ByteArray>, ByteArray> {

    override fun serialize(model: FixedLengthPataModel<ByteArray>, charset: Charset?, dataFieldSerializers: PataDataFieldSerializerMap): ByteArray {

        val propertyDatabase = model.propertyDatabase

        var ret = ByteArray(0)

        val targetCharset = charset ?: model.modelCharset

        propertyDatabase.forEach {
            val (property, annotation) = it

            val name = annotation.name
            val expectedSize = annotation.size
            val variableName = property.name

            val dataFieldSerializer = dataFieldSerializers.get<ByteArray>(property)

            val value = property.getter.call(model) ?:
                throw DataFieldNullException(property)

            val serializedValue = dataFieldSerializer.serializeWithCasting(value, targetCharset)
            val actualSize = serializedValue.size

            if (actualSize > expectedSize)
                throw DataModelSizeExceedException(
                    modelName = this::class.simpleName ?: "",
                    fieldName = name,
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
                        fieldName = name,
                        variableName = variableName,
                        expectedSize = expectedSize,
                        actualSize = actualSize,
                        data = serializedValue,
                        dataTable = model.toLog()
                    )
            }

            ret += dataFieldSerializer.padding(serializedValue, expectedSize, targetCharset)
        }

        return ret
    }
}
