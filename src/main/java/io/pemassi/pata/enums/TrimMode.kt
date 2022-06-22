/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.enums

enum class TrimMode
{
    /**
     * Do trim both ways
     */
    BOTH_TRIM,

    /**
     * Do left trim
     */
    START_TRIM,

    /**
     * DO right trim
     */
    END_TRIM,

    /**
     * Keep original value.
     */
    KEEP
}