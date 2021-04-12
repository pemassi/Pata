/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.exceptions

/**
 * This exception will be thrown when data size is higher than expected size.
 */
data class DataModelSizeExceedException(
    val modelName: String, val dataName: String, val variableName: String, val expectedSize: Int, val actualSize: Int, val data: String, val dataTable: String
): Exception("""
    $modelName's $dataName($variableName) value is exceeded size (expected: $expectedSize, actual: $actualSize).
    
    $dataTable
""".trimIndent())