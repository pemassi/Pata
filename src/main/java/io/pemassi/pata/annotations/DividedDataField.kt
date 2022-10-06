/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.annotations

/**
 * Declare this property is part of data model([DividedPataModel]).
 *
 * @param order This data field order, all data field will be sorted by [order].
 * @param name Field name
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class DividedDataField(
    val order: Int,
    val name: String,
)
