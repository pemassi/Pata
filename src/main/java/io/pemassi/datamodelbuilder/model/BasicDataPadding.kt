/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.model

import io.pemassi.datamodelbuilder.interfaces.DataPadding
import java.nio.charset.Charset
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

/**
 * This padding method will right-justify data and padding '0' for number values([Int], [Long]),
 * expect that, will left-justify data and padding ' '(space).
 */
class BasicDataPadding: DataPadding {
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