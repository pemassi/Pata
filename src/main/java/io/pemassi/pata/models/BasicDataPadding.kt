/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models

import io.pemassi.pata.interfaces.PataPadding
import java.nio.charset.Charset
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

/**
 * This padding method will right-justify the data and padding '0' for number values([Int], [Long]),
 * expect that, will left-justify data and padding ' '(space, 0x20).
 */
class BasicDataPadding: PataPadding {
    override fun padding(data: Any, expectedSize: Int, type: KType, charset: Charset): String {

        val value = data.toString()
        val valueByteArray = value.toByteArray(charset)
        val actualSize = valueByteArray.size

        return when (type.javaType)
        {
            Int::class.javaObjectType,
            Int::class.javaPrimitiveType,
            Long::class.javaObjectType,
            Long::class.javaPrimitiveType -> padStart(value, expectedSize - actualSize, '0')

            else -> padEnd(value, expectedSize - actualSize, ' ')
        }
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
}