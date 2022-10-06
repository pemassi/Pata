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
import java.math.BigDecimal
import java.nio.charset.Charset
import kotlin.reflect.KProperty

class PataDataFieldStringToBigDecimalDeserializer: PataDataFieldDeserializer<String, BigDecimal>
{
    override fun deserialize(
        data: String?,
        charset: Charset,
        replaceNullMode: ReplaceNullMode,
        trimMode: TrimMode,
        checkNullMode: CheckNullMode,
        property: KProperty<*>
    ): BigDecimal?
    {
        return PataDataFieldDeserializerUtil.toBigDecimal(
            data = data,
            checkNullMode = checkNullMode,
            property = property
        )
    }
}

