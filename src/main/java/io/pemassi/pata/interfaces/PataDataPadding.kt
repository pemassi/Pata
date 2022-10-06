/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

interface PataDataPadding<DataType> {

    fun padding(data: DataType, expectedSize: Int, charset: Charset): DataType

}
