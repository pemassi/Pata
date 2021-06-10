/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.exceptions

data class DataModelUnsupportedTypeException(
    override val message: String
): Exception(message)