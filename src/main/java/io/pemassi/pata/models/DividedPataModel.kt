/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models

import de.m3y.kformat.Table
import de.m3y.kformat.table
import io.pemassi.kotlin.extensions.common.encodeHexString
import io.pemassi.pata.Pata
import io.pemassi.pata.annotations.DividedDataField
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
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
abstract class DividedPataModel(
    val delimiters: String,
    override val modelCharset: Charset = Charset.defaultCharset(),
    val replaceNullMode: ReplaceNullMode = ReplaceNullMode.KEEP,
    val trimMode: TrimMode = TrimMode.BOTH_TRIM,
    val checkNullMode: CheckNullMode = CheckNullMode.REPLACE,
): PataModel<String>
{
    val propertyDatabase: List<Pair<KMutableProperty<*>, DividedDataField>> by lazy {
        cachedPropertyDatabase.getOrPut(this::class) {
            val tempDatabase = ArrayList<Pair<KMutableProperty<*>, DividedDataField>>()
            getPropertiesWithProtocolAnnotation { property, annotation ->
                tempDatabase.add(Pair(property, annotation))
            }
            tempDatabase.sortedWith(compareBy { it.second.order })
        }
    }

    @Deprecated("parameter 'pata' is not required anymore.", ReplaceWith("toLog()"), DeprecationLevel.WARNING)
    fun toLog(@Suppress("unused") pata: Pata): String
    {
        return this.toLog()
    }

    fun toLog(): String
    {
        return table {
            header("Name(Variable Name)", "Actual Size", "Value")

            propertyDatabase.forEach {
                val (property, annotation) = it

                val name = annotation.name
                val variableName = property.name
                val value = property.getter.call(this@DividedPataModel)

                val printValue: String
                val actualSize: Int

                if(value is ByteArray)
                {
                    printValue = "0x" + value.encodeHexString()
                    actualSize = value.size
                }
                else
                {
                    printValue = value.toString()

                    val valueByteArray = value.toString().toByteArray(modelCharset)
                    actualSize = valueByteArray.size
                }

                row("$name($variableName)", actualSize, "[$printValue]")
            }

            hints {
                borderStyle = Table.BorderStyle.SINGLE_LINE
                alignment(3, Table.Hints.Alignment.LEFT)
            }

        }.render(StringBuilder("'${this::class.simpleName}' Divided Data Model\n")).toString()
    }

    private inline fun Iterable<KMutableProperty<*>>.filterHasProtocolAnnotation(action: (KMutableProperty<*>, DividedDataField) -> Unit)
    {
        this.forEach { property ->
            property.annotations.forEach { annotation ->
                if (annotation is DividedDataField) action(property, annotation)
            }
        }
    }

    private fun getPropertiesWithProtocolAnnotation(action: (KMutableProperty<*>, DividedDataField) -> Unit)
    {
        this::class.java.kotlin.memberProperties.filterIsInstance<KMutableProperty<*>>()
            .filterHasProtocolAnnotation { property, protocol ->
                action(property, protocol)
            }
    }

    companion object
    {
        private val cachedPropertyDatabase by lazy {
            HashMap<KClass<*>, List<Pair<KMutableProperty<*>, DividedDataField>>>()
        }

    }
}
