/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType

/**
 * [PataPadding] interface will be used to padding data when size is smaller than expected size.
 *
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface PataPadding {
    fun padding(data: Any, expectedSize: Int, type: KType, charset: Charset): String
}