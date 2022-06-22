/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

/**
 * Determines whether to replace a value with null when it is empty.
 */
enum class ReplaceNullMode
{
    /**
     * If value is blank (zero-length or only space), replace with null.
     */
    WHEN_BLANK,

    /**
     * If value is empty (zero-length), replace with null.
     */
    WHEN_EMPTY,

    /**
     * Keep original value.
     */
    KEEP
}