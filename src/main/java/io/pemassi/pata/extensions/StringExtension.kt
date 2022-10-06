/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.extensions

import java.nio.charset.Charset

/**
 * Padding string with [padChar] by actual byte array length in [charset] until length become [length].
 */
fun String.padEndByByteLength(length: Int, padChar: Char, charset: Charset): String
{
    if (length < 0)
        throw IllegalArgumentException("Desired length $length is less than zero.")

    val byte = this.toByteArray(charset)

    val sb = StringBuilder(length)
    sb.append(this)

    for (i in 1..(length - byte.size))
        sb.append(padChar)

    return sb.toString()
}
