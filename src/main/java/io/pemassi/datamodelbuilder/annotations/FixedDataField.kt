/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.annotations

import io.pemassi.datamodelbuilder.interfaces.DataPadding
import io.pemassi.datamodelbuilder.model.BasicDataPadding
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class FixedDataField(
    val order: Int,
    val name: String,
    val size: Int,
    val padding: KClass<out DataPadding> = BasicDataPadding::class,
)
