/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

/**
 * Determine whether to throw exception or replace with default value when value is null.
 */
enum class CheckNullMode
{
    /**
     * Keep the original value, whether value is null
     *
     * **Example**
     * ```
     * null -> null
     *
     * // String
     * "abc" -> "abc"
     *
     * // Number (Int, Long, Float, Double)
     * "0" -> 0
     * "" -> throw exception
     * ```
     */
    KEEP,

    /**
     * If field value is null, replace with default value
     *
     * **Example**
     * ```
     * null -> 0 or "" (depends on field type)
     *
     * // String
     * "abc" -> "abc"
     *
     * // Number (Int, Long, Float, Double)
     * "0" -> 0
     * "" -> 0
     * ```
     */
    REPLACE,

    /**
     * If field value is null, throw exception.
     *
     * **Example**
     * ```
     * null -> throw exception
     *
     * // String
     * "abc" -> "abc"
     *
     * // Number (Int, Long, Float, Double)
     * "0" -> 0
     * "" -> throw exception
     * ```
     */
    EXCEPTION,
}
