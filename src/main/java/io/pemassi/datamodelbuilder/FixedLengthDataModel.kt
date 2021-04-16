/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.datamodelbuilder.annotations.FixedDataField
import io.pemassi.datamodelbuilder.exceptions.DataModelSizeExceedException
import io.pemassi.datamodelbuilder.exceptions.DataModelSizeNeedMoreException
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

private val cachedPropertyDatabase =
    HashMap<KClass<*>, List<Pair<KMutableProperty<*>, FixedDataField>>>()

enum class PaddingMode
{
    /**
     * If field' length is less than set length, it will pad character to fit in.
     */
    LENIENT,

    /**
     * If field's length is less or bigger than set length, throw exception.
     */
    STRICT
}

/**
 * Declare this class or data class is fixed length data model.
 *
 * The property, that you want use part of data, should be annotated with [FixedDataField].
 */
abstract class FixedLengthDataModel(
    val defaultCharset: Charset = Charset.defaultCharset(),
    val paddingMode: PaddingMode = PaddingMode.LENIENT,
)
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
                val value = property.getter.call(this@FixedLengthDataModel).toString()
                val valueByteArray = value.toByteArray(defaultCharset)
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
        return toDataString(defaultCharset)
    }

    fun toString(charset: Charset = defaultCharset): String
    {
        return toDataString(charset)
    }

    /**
     * Convert data model into fixed length string.
     *
     * @param charset
     */
    fun toDataString(charset: Charset = defaultCharset): String
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
         * Parsing byte data and convert into [FixedLengthDataModel].
         *
         * @param charset Charset will be used to convert byte array to string data.
         */
        inline fun <reified T : FixedLengthDataModel> parse(
            protocolData: ByteArray,
            charset: Charset = Charset.defaultCharset()
        ): T
        {
            var pos = 0
            val ret = T::class.createInstance()

            ret.propertyDatabase.forEach {
                val (property, annotation) = it
                val length = annotation.size
                val splitData = String(protocolData, pos, length, charset)
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
         * Parsing string data and convert into [FixedLengthDataModel].
         *
         * @param charset Charset will be used to convert string to byte array data, in order to precise size counting.
         */
        inline fun <reified T : FixedLengthDataModel> parse(
            protocolData: String,
            charset: Charset = Charset.defaultCharset()
        ): T
        {
            return parse(protocolData.toByteArray(charset), charset)
        }
    }
}
