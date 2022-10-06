/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.exceptions

/**
 * This exception is thrown when data size is higher than expected size.
 *
 * @param modelName Model name
 * @param fieldName Field name
 * @param variableName Variable name
 * @param expectedSize Expected size
 * @param actualSize Actual size
 * @param data Actual Data
 * @param dataTable DataTable (for debugging)
 */
data class DataModelSizeExceedException(
    val modelName: String,
    val fieldName: String,
    val variableName: String,
    val expectedSize: Int,
    val actualSize: Int,
    val data: Any,
    val dataTable: String,
): Exception("""
    $modelName's $fieldName($variableName) value is exceeded size (expected: $expectedSize, actual: $actualSize).
    
    $dataTable
""".trimIndent())
