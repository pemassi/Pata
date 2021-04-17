/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.interfaces.PataModel
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

/**
 * Declare this class or data class is fixed length data model.
 *
 * The property, that you want use part of data, should be annotated with [FixedDataField].
 */
abstract class FixedLengthPataModel(
    override val modelCharset: Charset = Charset.defaultCharset(),
    val paddingMode: PaddingMode = PaddingMode.LENIENT,
): PataModel
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
        private val cachedPropertyDatabase by lazy {
            HashMap<KClass<*>, List<Pair<KMutableProperty<*>, FixedDataField>>>()
        }

    }
}
