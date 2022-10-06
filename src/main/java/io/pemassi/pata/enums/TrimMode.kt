/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

/**
 * Determine how to trim the field if field type supports.
 */
enum class TrimMode
{
    /**
     * Do trim both ways
     *
     * **Example**
     * ```
     * "  123  " -> "123"
     * "  123" -> "123"
     * "123  " -> "123"
     * ```
     */
    BOTH_TRIM,

    /**
     * Do left trim
     *
     * **Example**
     * ```
     * "  123  " -> "123  "
     * "  123" -> "123"
     * "123  " -> "123  "
     * ```
     */
    START_TRIM,

    /**
     * Do right trim
     *
     * **Example**
     * ```
     * "  123  " -> "  123"
     * "  123" -> "  123"
     * "123  " -> "123"
     * ```
     */
    END_TRIM,

    /**
     * Keep original value.
     *
     * **Example**
     * ```
     * "  123  " -> "  123  "
     * "  123" -> "  123"
     * "123  " -> "123  "
     * ```
     */
    KEEP
}
