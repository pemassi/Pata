/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import io.pemassi.pata.models.map.PataDataFieldDeserializerMap
import java.nio.charset.Charset

interface PataModelDeserializer<InputType, ModelType: PataModel<*>> {

    fun deserialize(instance: ModelType, input: InputType, charset: Charset, dataFieldDeserializers: PataDataFieldDeserializerMap): ModelType

}