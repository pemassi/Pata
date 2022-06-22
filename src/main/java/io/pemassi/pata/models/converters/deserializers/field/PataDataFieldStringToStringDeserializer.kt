/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.models.converters.deserializers.field.abstracts.PataDataFieldDeserializerUtil
import java.nio.charset.Charset
import kotlin.reflect.KProperty

class PataDataFieldStringToStringDeserializer: PataDataFieldDeserializer<String, String>
{
    override fun deserialize(
        data: String,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): String?
    {
        return PataDataFieldDeserializerUtil.toString(
            data = data,
            replaceNullMode = replaceNullMode,
            trimMode = trimMode,
            checkNullMode = checkNullMode,
            property = property
        )
    }
}

