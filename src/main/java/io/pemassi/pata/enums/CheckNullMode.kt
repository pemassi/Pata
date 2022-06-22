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
     */
    KEEP,

    /**
     * If field value is null, replace with default value
     */
    REPLACE,

    /**
     * If field value is null, throw exception.
     */
    EXCEPTION,
}