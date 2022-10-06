/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

/**
 * Determine whether to pad or throw exception when field's length is less than set length.
 */
enum class PaddingMode
{
    /**
     * If field's length is less than set length, it will pad character to fit in.
     *
     * **Example**
     * ```
     * // field's set length is 5
     * "123" -> "123  " // 5 - 3 = 2, so pad 2 spaces.
     * ```
     */
    LENIENT,

    /**
     * If field's length is less or bigger than set length, throw exception.
     *
     * **Example**
     * ```
     * // field's set length is 5
     * "123" -> throw exception
     */
    STRICT
}
