/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType

/**
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface DataPadding {
    fun padding(data: Any, expectedSize: Int, type: KType, charset: Charset): String
}