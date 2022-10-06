/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.exceptions

/**
 * This exception is thrown when the data is not supported by the Pata.
 */
data class DataModelUnsupportedTypeException(
    override val message: String
): Exception(message)
