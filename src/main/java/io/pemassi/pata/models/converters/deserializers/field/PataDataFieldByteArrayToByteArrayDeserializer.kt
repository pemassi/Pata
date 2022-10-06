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
        data: ByteArray?,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): ByteArray? {

        val nullChecked = when(checkNullMode)
        {
            CheckNullMode.KEEP -> data
            CheckNullMode.REPLACE -> data ?: ByteArray(0)
            CheckNullMode.EXCEPTION -> throw DataFieldNullException(property)
        }

        val nullReplaced = if(nullChecked != null)
        {
            when(replaceNullMode)
            {
                ReplaceNullMode.KEEP -> nullChecked
                ReplaceNullMode.WHEN_EMPTY -> {
                    if(nullChecked.isEmpty()) {
                        null
                    }
                    else {
                        nullChecked
                    }
                }
                ReplaceNullMode.WHEN_BLANK -> nullChecked
            }
        }
        else
        {
            // afterCheckNull is always null in this case
            // but keep code style
            @Suppress("KotlinConstantConditions")
            nullChecked
        }

        return nullReplaced
    }
}
