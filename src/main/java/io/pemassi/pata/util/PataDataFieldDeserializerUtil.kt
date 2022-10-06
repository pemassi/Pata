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
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object PataDataFieldDeserializerUtil
{
    fun toString(
        data: String,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): String?
    {
        val trimmed = when(trimMode)
        {
            TrimMode.KEEP -> data
            TrimMode.BOTH_TRIM -> data.trim()
            TrimMode.START_TRIM -> data.trimStart()
            TrimMode.END_TRIM -> data.trimEnd()
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

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> nullReplaced
            CheckNullMode.REPLACE -> nullReplaced ?: ""
            CheckNullMode.EXCEPTION -> nullReplaced ?: throw DataFieldNullException(property)
        }
    }

    fun toLong(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Long?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toLong()
            CheckNullMode.REPLACE -> processed?.toLong() ?: 0
            CheckNullMode.EXCEPTION -> processed?.toLong() ?: throw DataFieldNullException(property)
        }
    }

    fun toInt(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Int?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toInt()
            CheckNullMode.REPLACE -> processed?.toInt() ?: 0
            CheckNullMode.EXCEPTION -> processed?.toInt() ?: throw DataFieldNullException(property)
        }
    }

    fun toFloat(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Float?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toFloat()
            CheckNullMode.REPLACE -> processed?.toFloat() ?: 0.0f
            CheckNullMode.EXCEPTION -> processed?.toFloat() ?: throw DataFieldNullException(property)
        }
    }

    fun toDouble(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Double?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toDouble()
            CheckNullMode.REPLACE -> processed?.toDouble() ?: 0.0
            CheckNullMode.EXCEPTION -> processed?.toDouble() ?: throw DataFieldNullException(property)
        }
    }

    fun toBigDecimal(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): BigDecimal?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toBigDecimal()
            CheckNullMode.REPLACE -> processed?.toBigDecimal() ?: BigDecimal.ZERO
            CheckNullMode.EXCEPTION -> processed?.toBigDecimal() ?: throw DataFieldNullException(property)
        }
    }

    fun toBigInteger(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): BigInteger?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processed?.toBigInteger()
            CheckNullMode.REPLACE -> processed?.toBigInteger() ?: BigInteger.ZERO
            CheckNullMode.EXCEPTION -> processed?.toBigInteger() ?: throw DataFieldNullException(property)
        }
    }

    fun toEnum(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>,
    ): Enum<*>?
    {
        val processed = toString(
            data = data,
            replaceNullMode = ReplaceNullMode.WHEN_BLANK,
            trimMode = TrimMode.BOTH_TRIM,
            checkNullMode = checkNullMode,
            property = property
        )

        val enumClass = property.returnType.classifier as KClass<Enum<*>>
        val enumList = enumClass.java.enumConstants as Array<Enum<*>>
        val found = enumList.find { it.name == processed }

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> found
            CheckNullMode.REPLACE -> found ?: throw DataFieldNullException(property)
            CheckNullMode.EXCEPTION -> found ?: throw DataFieldNullException(property)
        }
    }
}
