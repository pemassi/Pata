/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.pata.Pata
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.PaddingMode
import io.pemassi.pata.exceptions.DataModelUnsupportedTypeException
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.interfaces.PataModel
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * Declare this class or data class is fixed length data model.
 *
 * The property, that you want use part of data, should be annotated with [FixedDataField].
 */
abstract class FixedLengthPataModel<DataType>(
    override val modelCharset: Charset = Charset.defaultCharset(),
    val paddingMode: PaddingMode = PaddingMode.LENIENT,
): PataModel<DataType>
{
    val propertyDatabase: List<Pair<KMutableProperty<*>, FixedDataField>> by lazy {
        cachedPropertyDatabase.getOrPut(this::class) {
            val tempDatabase = ArrayList<Pair<KMutableProperty<*>, FixedDataField>>()
            getPropertiesWithProtocolAnnotation { property, annotation ->
                tempDatabase.add(Pair(property, annotation))
            }
            tempDatabase.sortedWith(compareBy { it.second.order })
        }
    }

    val totalLength: Int by lazy {
        propertyDatabase.sumOf { it.second.size }
    }

    fun toLog(pata: Pata = Pata()): String
    {
        return table {
            header("Name(Variable Name)", "Expected Size", "Actual Size", "Value")

            propertyDatabase.forEach {
                val (property, annotation) = it

                val name = annotation.name
                val expectedSize = annotation.size
                val variableName = property.name
                val value = property.getter.call(this@FixedLengthPataModel)

                val printValue: String
                val actualSize: Int


                if(value is ByteArray)
                {
                    printValue = "0x" + bytesToHex(value)
                    actualSize = value.size
                }
                else
                {
                    printValue = value.toString()

                    val valueByteArray = value.toString().toByteArray(modelCharset)
                    actualSize = valueByteArray.size
                }

                row("$name($variableName)", expectedSize, actualSize, "[$printValue]")
            }

            hints {
                borderStyle = Table.BorderStyle.SINGLE_LINE
                alignment(3, Table.Hints.Alignment.LEFT)
            }

        }.render(StringBuilder("'${this::class.simpleName}' Fixed Length Data Model\n")).toString()
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

    private val hexArray = "0123456789ABCDEF".toCharArray()

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF

            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    companion object
    {
        private val cachedPropertyDatabase by lazy {
            HashMap<KClass<*>, List<Pair<KMutableProperty<*>, FixedDataField>>>()
        }

    }
}
