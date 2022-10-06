/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.annotations

/**
 * Declare this property is part of data model([FixedLengthDataModel]).
 *
 * @param order This data field order, all data field will be sorted by [order].
 * @param name Field name
 * @param size Field Size(expected size)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class FixedDataField(
    val order: Int,
    val name: String,
    val size: Int,
)
