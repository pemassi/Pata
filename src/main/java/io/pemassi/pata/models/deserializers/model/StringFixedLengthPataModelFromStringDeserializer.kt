/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.deserializers.model

import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.interfaces.PataModelDeserializer
import io.pemassi.pata.interfaces.createInstance
import io.pemassi.pata.models.FixedLengthPataModel
import java.nio.charset.Charset
import kotlin.reflect.KType

class StringFixedLengthPataModelFromStringDeserializer: PataModelDeserializer<String, FixedLengthPataModel<String>, String> {

    override fun deserialize(
        input: String,
        charset: Charset?,
        dataFieldDeserializers: HashMap<KType, PataDataFieldDeserializer<*>>
    ): FixedLengthPataModel<String> {

        return StringFixedLengthPataModelFromByteArrayDeserializer().deserialize(
            input = input.toByteArray(charset ?: createInstance().modelCharset),
            charset = charset,
            dataFieldDeserializers = dataFieldDeserializers
        )

    }
}