/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.exceptions.DataFieldNullException
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.util.PataDataFieldDeserializerUtil
import java.nio.charset.Charset
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class PataDataFieldStringToEnumDeserializer: PataDataFieldDeserializer<String, Enum<*>>
{
    override fun deserialize(
        data: String,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Enum<*>?
    {
        return PataDataFieldDeserializerUtil.toEnum(
            data = data,
            checkNullMode = checkNullMode,
            property = property,
        )
    }
}

