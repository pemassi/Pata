/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class FixedDataField(val order: Int, val name: String, val length: Int)
