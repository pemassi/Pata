/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.exceptions

/**
 * This exception is thrown when data size is lower than expected size.
 *
 */
data class DataModelSizeNeedMoreException(
    val modelName: String,
    val fieldName: String,
    val variableName: String,
    val expectedSize: Int,
    val actualSize: Int,
    val data: Any,
    val dataTable: String,
): Exception("""
    $modelName's $fieldName($variableName) value is not matched size (expected: $expectedSize, actual: $actualSize).
    
    $dataTable
""".trimIndent())
