/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata.old

import io.pemassi.pata.Pata
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.PaddingMode
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldStringToStringSerializer
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class FixedLengthPataModelKotlinTest
{
    val EUC_KR = Charset.forName("EUC_KR")

    class StandardProtocol : FixedLengthPataModel<String>()
    {
        @FixedDataField(5, "A", 5)
        var a: String = ""

        @FixedDataField(3, "B", 5)
        var b: String = ""

        @FixedDataField(2, "C", 5)
        var c: String = ""

        @FixedDataField(4, "D", 5)
        var d: String = ""

        @FixedDataField(1, "E", 5)
        var e: String = ""

        @FixedDataField(6, "F", 5)
        var f: String = ""

        @FixedDataField(10, "G", 5)
        var g: Int = 0

        @FixedDataField(8, "H", 5)
        var h: String = ""

        @FixedDataField(9, "I", 5)
        var i: String = ""

        @FixedDataField(7, "J", 5)
        var j: String = ""

        companion object
        {
            val correctOrder = listOf("E", "C", "B", "D", "A", "F", "J", "H", "I", "G")
            val parseData = "E    C    B    D    A    F    J    H    I    00001"
        }
    }

    @Test
    fun `Deserialize Protocol In Order`()
    {
        val pata = Pata()

        //When created
        val createdObject = StandardProtocol()
        assertArrayEquals(
            StandardProtocol.correctOrder.toTypedArray(),
            createdObject.propertyDatabase.map { it.second.name }.toTypedArray()
        )

        //When parsed
        val parsedObject = pata.deserialize<String, StandardProtocol>(StandardProtocol.parseData)
        assertArrayEquals(
            StandardProtocol.correctOrder.toTypedArray(),
            parsedObject.propertyDatabase.map { it.second.name }.toTypedArray()
        )
    }

    @Test
    fun `Serialize correctly`()
    {
        val pata = Pata()

        //Test data is same as parsed data
        assertEquals("                                             00000", pata.serialize(StandardProtocol()))
    }

    @Test
    fun `Korean Test`()
    {
        val pata = Pata()

        val korean = "한글"
        val created = StandardProtocol().also {
            it.a = korean
        }

        val parsed = pata.deserialize<String, StandardProtocol>(pata.serialize(created, EUC_KR), EUC_KR)

        assertEquals(created.a.trim(), parsed.a.trim())
        assertEquals(created.b.trim(), parsed.b.trim())
        assertEquals(created.c.trim(), parsed.c.trim())
        assertEquals(created.d.trim(), parsed.d.trim())
        assertEquals(created.e.trim(), parsed.e.trim())
        assertEquals(created.f.trim(), parsed.f.trim())
        assertEquals(created.g, parsed.g)
        assertEquals(created.h.trim(), parsed.h.trim())
        assertEquals(created.i.trim(), parsed.i.trim())
        assertEquals(created.j.trim(), parsed.j.trim())

        assertEquals(pata.serialize(created, EUC_KR), pata.serialize(parsed, EUC_KR))
    }

//    data class KotlinDataClassModel(
//        @FixedDataField(1, "A", 5)
//        var a: String = "A",
//
//        @FixedDataField(2, "B", 5)
//        var b: String = "B"
//    ): FixedLengthPataModel<String>()
//    {
//        companion object
//        {
//            val correctData = "A    B    "
//        }
//    }
//
//    @Test
//    fun `Kotlin Data Class Test`()
//    {
//        val pata = Pata()
//
//        assertEquals(KotlinDataClassModel.correctData, pata.serialize<KotlinDataClassModel, FixedLengthPataModel<String>, String>(KotlinDataClassModel()))
//        assertEquals(KotlinDataClassModel(), pata.deserialize<String, FixedLengthPataModel<String>, KotlinDataClassModel>(KotlinDataClassModel.correctData))
//    }

    enum class Currency(val code: String)
    {
        KOREAN_WON("KRW"),
        USA_DOLLAR("USD"),
        JAPAN_YEN("JPY"),
    }

    class PataDataFieldCurrencyToStringSerializer: PataDataFieldSerializer<Currency, String>
    {
        override fun serialize(input: Currency, charset: Charset): String {
            return input.code
        }

        override fun padding(data: String, expectedSize: Int, charset: Charset): String {
            return PataDataFieldStringToStringSerializer().padding(data, expectedSize, charset)
        }
    }

    data class KotlinDataClassModel(
        @FixedDataField(order = 1, name = "Item Name",  size = 10)
        var itemName: String = "",

        @FixedDataField(order = 2, name ="Price", size = 5)
        var price: Int = 0,

        @FixedDataField(order = 3, name ="Currency", size = 3)
        var currency: Currency = Currency.KOREAN_WON,
    ): FixedLengthPataModel<String>(
        modelCharset = Charsets.UTF_8,
        paddingMode = PaddingMode.LENIENT
    )
    @Test
    fun Example()
    {
        val model = KotlinDataClassModel().apply {
            itemName = "Ice Cream"
            price = 1200
            currency = Currency.KOREAN_WON
        }

        val pata = Pata().apply {
            registerDataFieldSerializer(PataDataFieldCurrencyToStringSerializer())
        }
        val serialized = pata.serialize(model)

        println("[$serialized]")
    }

    @Test
    fun `Kotlin Data Class Test`()
    {
        val model = KotlinDataClassModel().apply {
            itemName = "Ice Cream"
            price = 1200
            currency = Currency.KOREAN_WON
        }

        val pata = Pata().apply {
            registerDataFieldSerializer(PataDataFieldCurrencyToStringSerializer())
        }
        val serialized = pata.serialize(model)

        println("[$serialized]")
    }


    class Test2 : FixedLengthPataModel<ByteArray>()
    {
        @FixedDataField(5, "A", 5)
        var a: ByteArray = ByteArray(5) { -0x2F }
    }

    @Test
    fun `123`()
    {
        println(Test2().toLog())
    }


}