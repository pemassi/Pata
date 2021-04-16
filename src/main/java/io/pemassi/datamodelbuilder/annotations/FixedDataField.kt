/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.annotations

import io.pemassi.datamodelbuilder.interfaces.DataPadding
import io.pemassi.datamodelbuilder.model.BasicDataPadding
import kotlin.reflect.KClass

/**
 * Declare this property is part of data model(FixedLengthDataModel).
 *
 * @param order The property order
 * @param name Data name
 * @param size Data Size(expected size)
 * @param padding You can set custom padding method with class that inherited with [DataPadding] interface.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class FixedDataField(
    val order: Int,
    val name: String,
    val size: Int,
    val padding: KClass<out DataPadding> = BasicDataPadding::class,
)
