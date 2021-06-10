/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance

interface PataModelDeserializer<InputType, ModelType: PataModel<DataType>, DataType> {

    fun deserialize(input: InputType, charset: Charset?, dataFieldDeserializers: HashMap<KType, HashMap<KType, PataDataFieldDeserializer<*, *>>>): ModelType

}

inline fun <reified ModelType: PataModel<DataType>, DataType> PataModelDeserializer<*, ModelType, DataType>.createInstance() = ModelType::class.createInstance()