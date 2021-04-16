/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.annotations

import io.pemassi.pata.interfaces.PataPadding
import io.pemassi.pata.models.BasicDataPadding
import kotlin.reflect.KClass

/**
 * Declare this property is part of data model(FixedLengthDataModel).
 *
 * @param order This data field order, all data field will be sorted by [order].
 * @param name Data name
 * @param size Data Size(expected size)
 * @param padding You can set custom padding method with class that inherited with [PataPadding] interface.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class FixedDataField(
    val order: Int,
    val name: String,
    val size: Int,
    val padding: KClass<out PataPadding> = BasicDataPadding::class,
)
