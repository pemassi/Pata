/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.datamodelbuilder.annotations.FixedDataField
import io.pemassi.datamodelbuilder.exceptions.DataModelSizeExceedException
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
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
 *
 */
abstract class FixedLengthDataModel(
    val charset: Charset = Charset.defaultCharset(),
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
                val valueByteArray = value.toByteArray(charset)
                val actualSize = valueByteArray.size

                row("$name($variableName)", expectedSize, actualSize, "[$value]")
            }

            hints {
                borderStyle = Table.BorderStyle.SINGLE_LINE
                alignment(3, Table.Hints.Alignment.LEFT)
            }

        }.render(StringBuilder("'${this::class.simpleName}' Fixed Length Data Model\n")).toString()
    }

    override fun toString(): String
    {
        return toString(charset)
    }

    fun toString(charset: Charset): String
    {
        val buffer = StringBuffer()

        propertyDatabase.forEach {
            val (property, annotation) = it

            val name = annotation.name
            val expectedSize = annotation.size
            val variableName = property.name
            val value = property.getter.call(this).toString()
            val valueByteArray = value.toByteArray(charset)
            val actualSize = valueByteArray.size

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

            //TODO: Need to work on mode.

            buffer.append(
                when (property.returnType.javaType)
                {
                    Int::class.javaObjectType,
                    Int::class.javaPrimitiveType,
                    Long::class.javaObjectType,
                    Long::class.javaPrimitiveType -> padStart(value, expectedSize - actualSize, '0')

                    else -> padEnd(value, expectedSize - actualSize, ' ')
                }
            )
        }

        return buffer.toString()
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
        inline fun <reified T : FixedLengthDataModel> parse(
            protocolData: ByteArray,
            charset: Charset = Charset.defaultCharset()
        ): T
        {
            var pos = 0
            val ret = T::class.constructors.first().call()

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

        inline fun <reified T : FixedLengthDataModel> parse(
            protocolData: String,
            charset: Charset = Charset.defaultCharset()
        ): T
        {
            return parse(protocolData.toByteArray(charset), charset)
        }
    }
}

class TestProtocol : FixedLengthDataModel()
{
    @FixedDataField(5, "A", 5)
    var a: String = ""

    @FixedDataField(3, "B", 5)
    var b: String = ""

    @FixedDataField(2, "C", 5)
    var c: String = ""

    @FixedDataField(4, "D", 5)
    var d: String = ""

    @FixedDataField(1, "E", 5)
    var e: String = ""

    @FixedDataField(6, "F", 5)
    var f: String = ""

    @FixedDataField(10, "G", 5)
    var g: Int = 0

    @FixedDataField(8, "H", 5)
    var h: String = ""

    @FixedDataField(9, "I", 5)
    var i: String = ""

    @FixedDataField(7, "J", 5)
    var j: String = ""
}

fun main()
{
    print(TestProtocol().toLog())
}


