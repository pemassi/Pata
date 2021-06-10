/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

enum class PaddingMode
{
    /**
     * If field' length is less than set length, it will pad character to fit in.
     */
    LENIENT,

    /**
     * If field's length is less or bigger than set length, throw exception.
     */
    STRICT
}