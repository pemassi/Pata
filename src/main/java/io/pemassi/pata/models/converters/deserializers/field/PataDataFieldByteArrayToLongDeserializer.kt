/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.models.converters.deserializers.field

import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.interfaces.PataDataFieldDeserializer
import io.pemassi.pata.util.PataDataFieldDeserializerUtil
import java.nio.charset.Charset
import kotlin.reflect.KProperty

class PataDataFieldByteArrayToLongDeserializer: PataDataFieldDeserializer<ByteArray, Long>
{
    override fun deserialize(
        data: ByteArray,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): Long?
    {
        return PataDataFieldDeserializerUtil.toLong(
            data = String(data, charset),
            checkNullMode = checkNullMode,
            property = property
        )
    }
}
