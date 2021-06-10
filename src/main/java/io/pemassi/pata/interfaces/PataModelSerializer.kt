/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import io.pemassi.pata.models.map.PataDataFieldSerializerMap
import java.nio.charset.Charset

interface PataModelSerializer<T: PataModel<DataType>, DataType> {

    fun serialize(model: T, charset: Charset?, dataFieldSerializers: PataDataFieldSerializerMap): DataType

}