/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType

/**
 * [DataPadding] interface will be used to padding data when size is smaller than expected size.
 *
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface DataPadding {
    fun padding(data: Any, expectedSize: Int, type: KType, charset: Charset): String
}