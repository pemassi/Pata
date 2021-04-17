/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.interfaces

import java.nio.charset.Charset
import kotlin.reflect.KType

interface PataModelSerializer<T: PataModel> {

    fun serialize(model: T, charset: Charset?, dataFieldSerializers: HashMap<KType, PataDataFieldSerializer<*>>): String

}