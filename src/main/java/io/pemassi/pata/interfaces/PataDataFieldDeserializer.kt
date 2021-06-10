/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset

/**
 * [PataDataFieldDeserializer] interface will be used to padding data when size is smaller than expected size.
 *
 * (IMPORTANT) Inherited class must have non-parameter constructor.
 *
 */
interface PataDataFieldDeserializer<InputType, DataType> {

    fun deserialize(data: InputType, charset: Charset): DataType

}