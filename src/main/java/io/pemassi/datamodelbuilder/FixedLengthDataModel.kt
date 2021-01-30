/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder

import io.pemassi.datamodelbuilder.annotations.FixedDataField
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.HashMap
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
            sum += it.second.length
        }
        sum
    }

    fun toLog(): String
    {
        val buffer = StringBuffer(this::class.simpleName + " Fixed Length Data\n")

        propertyDatabase.forEach {
            val (property, annotation) = it

            val name = annotation.name
            val length = annotation.length
            val variableName = property.name
            val value = property.getter.call(this)

            buffer.append("$name($variableName, ${property.returnType}) : [$length][$value]\n")
        }

        return buffer.toString()
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
            val length = annotation.length
            val value = property.getter.call(this).toString()
            val valueByteArray = value.toByteArray(charset)
            val valueLength = valueByteArray.size

            if (valueLength > length)
                throw InvalidPropertiesFormatException(
                    """
                    Property value is exceeded length.
                    Name: ${annotation.name}(${property.name})
                    Current Length: $valueLength
                    Expected Length: $length
                    
                    [DATA]
                    ${this.toLog()}
                """.trimIndent()
                )

            //TODO: Need to work on mode.

            buffer.append(
                when (property.returnType.javaType)
                {
                    Int::class.javaObjectType,
                    Int::class.javaPrimitiveType,
                    Long::class.javaObjectType,
                    Long::class.javaPrimitiveType -> padStart(value, length - valueLength, '0')

                    else -> padEnd(value, length - valueLength, ' ')
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
                val length = annotation.length
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