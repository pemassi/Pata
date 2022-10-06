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
import java.nio.charset.Charset
import kotlin.reflect.KProperty

class PataDataFieldByteArrayToByteArrayDeserializer: PataDataFieldDeserializer<ByteArray, ByteArray>
{
    override fun deserialize(
        data: ByteArray,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): ByteArray? {
        val nullReplaced = when(replaceNullMode)
        {
            ReplaceNullMode.KEEP -> data
            ReplaceNullMode.WHEN_EMPTY -> {
                if(data.isEmpty()) {
                    null
                }
                else {
                    data
                }
            }
            ReplaceNullMode.WHEN_BLANK -> data
        }

        return when(checkNullMode)
        {
            CheckNullMode.KEEP -> nullReplaced
            CheckNullMode.REPLACE -> nullReplaced ?: ByteArray(0)
            CheckNullMode.EXCEPTION -> nullReplaced ?: throw DataFieldNullException(property)
        }
    }
}
