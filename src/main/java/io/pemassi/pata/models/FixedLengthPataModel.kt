/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.exceptions.DataModelSizeExceedException
import io.pemassi.pata.exceptions.DataModelSizeNeedMoreException
import io.pemassi.pata.interfaces.PataModel
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

private val cachedPropertyDatabase =
    HashMap<KClass<*>, List<Pair<KMutableProperty<*>, FixedDataField>>>()

/**
 * Declare this class or data class is fixed length data model.
 *
 * The property, that you want use part of data, should be annotated with [FixedDataField].
 */
abstract class FixedLengthPataModel(
    val modelCharset: Charset = Charset.defaultCharset(),
    val paddingMode: PaddingMode = PaddingMode.LENIENT,
): PataModel
{
    val propertyDatabase: List<Pair<KMutableProperty<*>, FixedDataField>> =
        cachedPropertyDatabase.getOrPut(this::class) {
            val tempDatabase = ArrayList<Pair<KMutableProperty<*>, FixedDataField>>()
            getPropertiesWithProtocolAnnotation { property, annotation ->
                tempDatabase.add(Pair(property, annotation))
            }
            tempDatabase.sortedWith(compareBy { it.second.order })
        }

    val totalLength: Int by lazy {
        var sum = 0
        propertyDatabase.forEach {
            sum += it.second.size
        }
        sum
    }

    fun toLog(): String
    {
        return table {
            header("Name(Variable Name)", "Expected Size", "Actual Size", "Value")

            propertyDatabase.forEach {
                val (property, annotation) = it

                val name = annotation.name
                val expectedSize = annotation.size
                val variableName = property.name
                val value = property.getter.call(this@FixedLengthPataModel).toString()
                val valueByteArray = value.toByteArray(modelCharset)
                val actualSize = valueByteArray.size

                row("$name($variableName)", expectedSize, actualSize, "[$value]")
            }

            hints {
                borderStyle = Table.BorderStyle.SINGLE_LINE
                alignment(3, Table.Hints.Alignment.LEFT)
            }

        }.render(StringBuilder("'${this::class.simpleName}' Fixed Length Data Model\n")).toString()
    }

    /**
     * WARN - This method will be overridden when class is Kotlin's data class.
     */
    override fun toString(): String
    {
        return toDataString(modelCharset)
    }

    fun toString(charset: Charset = modelCharset): String
    {
        return toDataString(charset)
    }

    /**
     * Convert data model into fixed length string.
     *
     * @param charset
     */
    fun toDataString(charset: Charset = modelCharset): String
    {
        val builder = StringBuilder()

        propertyDatabase.forEach {
            val (property, annotation) = it

            val name = annotation.name
            val expectedSize = annotation.size
            val variableName = property.name
            val value = property.getter.call(this).toString()
            val valueByteArray = value.toByteArray(charset)
            val actualSize = valueByteArray.size
            val dataPadding = annotation.padding.createInstance()

            if (actualSize > expectedSize)
                throw DataModelSizeExceedException(
                    modelName = this::class.simpleName ?: "",
                    dataName = name,
                    variableName = variableName,
                    expectedSize = expectedSize,
                    actualSize = actualSize,
                    data = value,
                    dataTable = this.toLog()
                )

            if(paddingMode == PaddingMode.STRICT)
            {
                if(actualSize != expectedSize)
                    throw DataModelSizeNeedMoreException(
                        modelName = this::class.simpleName ?: "",
                        dataName = name,
                        variableName = variableName,
                        expectedSize = expectedSize,
                        actualSize = actualSize,
                        data = value,
                        dataTable = this.toLog()
                    )
            }

            builder.append(
                dataPadding.padding(value, expectedSize, property.returnType, charset)
            )
        }

        return builder.toString()
    }

    private fun padStart(str: String, amount: Int, padChar: Char): String
    {
        if (amount <= 0)
            return str

        return "".padEnd(amount, padChar) + str
    }

    private fun padEnd(str: String, amount: Int, padChar: Char): String
    {
        if (amount <= 0)
            return str

        return str + "".padEnd(amount, padChar)
    }

    private inline fun Iterable<KMutableProperty<*>>.filterHasProtocolAnnotation(action: (KMutableProperty<*>, FixedDataField) -> Unit)
    {
        this.forEach { property ->
            property.annotations.forEach { annotation ->
                if (annotation is FixedDataField) action(property, annotation)
            }
        }
    }

    private fun getPropertiesWithProtocolAnnotation(action: (KMutableProperty<*>, FixedDataField) -> Unit)
    {
        this::class.java.kotlin.memberProperties.filterIsInstance<KMutableProperty<*>>()
            .filterHasProtocolAnnotation { property, protocol ->
                action(property, protocol)
            }
    }

    companion object
    {
        /**
         * Parsing byte data and convert into [FixedLengthPataModel].
         *
         * @param charset Charset will be used to convert byte array to string data.
         */
        inline fun <reified T : FixedLengthPataModel> parse(
            protocolData: ByteArray,
            charset: Charset? = null
        ): T
        {
            var pos = 0
            val ret = T::class.createInstance()

            ret.propertyDatabase.forEach {
                val (property, annotation) = it
                val length = annotation.size
                val splitData = String(protocolData, pos, length, charset ?: ret.modelCharset)
                val inputData = when (property.returnType.javaType)
                {
                    Int::class.javaObjectType,
                    Int::class.javaPrimitiveType -> splitData.toInt()

                    Long::class.javaObjectType,
                    Long::class.javaPrimitiveType -> splitData.toLong()

                    else -> splitData
                }

                property.setter.call(ret, inputData)

                pos += length
            }

            return ret
        }

        /**
         * Parsing string data and convert into [FixedLengthPataModel].
         *
         * @param charset Charset will be used to convert string to byte array data, in order to precise size counting.
         */
        inline fun <reified T : FixedLengthPataModel> parse(
            protocolData: String,
            charset: Charset? = null
        ): T
        {
            val ret = T::class.createInstance()

            return parse(protocolData.toByteArray(charset ?: ret.modelCharset), charset)
        }
    }
}
