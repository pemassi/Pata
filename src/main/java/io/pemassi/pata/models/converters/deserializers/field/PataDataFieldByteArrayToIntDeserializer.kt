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

class PataDataFieldByteArrayToIntDeserializer: PataDataFieldDeserializer<ByteArray, Int>
{
    override fun deserialize(
        data: ByteArray,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Int?
    {
        return PataDataFieldDeserializerUtil.toInt(
            data = String(data, charset),
            checkNullMode = checkNullMode,
            property = property
        )
    }
}