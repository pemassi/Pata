/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.serializers.model

import io.pemassi.pata.exceptions.DataModelSizeExceedException
import io.pemassi.pata.exceptions.DataModelSizeNeedMoreException
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.interfaces.PataModelSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.PaddingMode
import java.nio.charset.Charset
import java.security.spec.InvalidParameterSpecException
import kotlin.reflect.KType

class FixedLengthPataModelSerializer: PataModelSerializer<FixedLengthPataModel> {

    override fun serialize(model: FixedLengthPataModel, charset: Charset?, dataFieldSerializers: HashMap<KType, PataDataFieldSerializer<*>>): String {

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
            val dataFieldSerializer = dataFieldSerializers[variableType] as? PataDataFieldSerializer<Any?>
                ?: throw InvalidParameterSpecException()

            val value = property.getter.call(model)
            val serializedValue = dataFieldSerializer.serialize(value, targetCharset)
            val valueByteArray = serializedValue.toByteArray(targetCharset)
            val actualSize = valueByteArray.size

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

            builder.append(
                dataFieldSerializer.padding(serializedValue, expectedSize, targetCharset)
            )
        }

        return builder.toString()
    }
}