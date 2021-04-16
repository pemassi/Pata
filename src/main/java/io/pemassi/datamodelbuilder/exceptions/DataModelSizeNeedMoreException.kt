/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.exceptions

/**
 * This exception will be thrown when data size is lower than expected size.
 */
data class DataModelSizeNeedMoreException(
    val modelName: String, val dataName: String, val variableName: String, val expectedSize: Int, val actualSize: Int, val data: String, val dataTable: String
): Exception("""
    $modelName's $dataName($variableName) value is not matched size (expected: $expectedSize, actual: $actualSize).
    
    $dataTable
""".trimIndent())