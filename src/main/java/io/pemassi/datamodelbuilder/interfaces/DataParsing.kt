/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType

/**
 * [DataParsing] interface will be used to padding data when size is smaller than expected size.
 *
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface DataParsing {
    fun parseByteArray(data: ByteArray, type: KType, charset: Charset): String
    fun parseString(data: String, type: KType, charset: Charset): String
}