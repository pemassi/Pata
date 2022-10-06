/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.util

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.exceptions.DataFieldNullException
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KProperty

object PataDataFieldDeserializerUtil
{
    fun toString(
        data: String?,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): String?
    {
        val nullChecked = when(checkNullMode)
        {
            CheckNullMode.KEEP -> return data
            CheckNullMode.REPLACE -> data ?: ""
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }

        val trimmed = when(trimMode)
        {
            TrimMode.KEEP -> nullChecked
            TrimMode.BOTH_TRIM -> nullChecked.trim()
            TrimMode.START_TRIM -> nullChecked.trimStart()
            TrimMode.END_TRIM -> nullChecked.trimEnd()
        }

        val nullReplaced = when(replaceNullMode)
        {
            ReplaceNullMode.KEEP -> trimmed

            ReplaceNullMode.WHEN_EMPTY -> {
                trimmed.ifEmpty {
                    null
                }
            }

            ReplaceNullMode.WHEN_BLANK -> {
                trimmed.ifBlank {
                    null
                }
            }
        }

        return nullReplaced
    }

    fun toLong(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Long?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toLong()
            CheckNullMode.REPLACE -> data?.toLongOrNull() ?: 0
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toInt(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Int?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toInt()
            CheckNullMode.REPLACE -> data?.toIntOrNull() ?: 0
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toFloat(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Float?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toFloat()
            CheckNullMode.REPLACE -> data?.toFloatOrNull() ?: 0.0f
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toDouble(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Double?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toDouble()
            CheckNullMode.REPLACE -> data?.toDoubleOrNull() ?: 0.0
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toBigDecimal(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): BigDecimal?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toBigDecimal()
            CheckNullMode.REPLACE -> data?.toBigDecimalOrNull() ?: BigDecimal.ZERO
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toBigInteger(
        data: String?,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): BigInteger?
    {
        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> data?.toBigInteger()
            CheckNullMode.REPLACE -> data?.toBigIntegerOrNull() ?: BigInteger.ZERO
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }
}
