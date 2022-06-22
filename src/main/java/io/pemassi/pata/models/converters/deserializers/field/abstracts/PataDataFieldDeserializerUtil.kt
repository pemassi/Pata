/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field.abstracts

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.exceptions.DataFieldNullException
import java.nio.charset.Charset
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
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toLong(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Long?
    {
        val processedValue = toString(
            data,
            ReplaceNullMode.WHEN_BLANK,
            TrimMode.BOTH_TRIM,
            checkNullMode,
            property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processedValue?.toLong()
            CheckNullMode.REPLACE -> processedValue?.toLong() ?: 0
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }

    fun toInt(
        data: String,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Int?
    {
        val processedValue = toString(
            data,
            ReplaceNullMode.WHEN_BLANK,
            TrimMode.BOTH_TRIM,
            checkNullMode,
            property
        )

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> processedValue?.toInt()
            CheckNullMode.REPLACE -> processedValue?.toInt() ?: 0
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }
    }
}