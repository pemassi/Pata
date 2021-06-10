/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

/**
 * [PataDataPadding] interface will be used to padding data when size is smaller than expected size.
 *
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface PataDataPadding< DataType> {

    fun padding(data: DataType, expectedSize: Int, charset: Charset): DataType

}