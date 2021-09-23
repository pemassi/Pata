/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.pemassi.pata.annotations.DividedDataField
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.models.DividedPataModel
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldStringToStringSerializer
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class DividedPataModelKotlinTest
{
    val EUC_KR = Charset.forName("EUC_KR")

    class StandardProtocol : DividedPataModel("|")
    {
        @DividedDataField(5, "A")
        var a: String = ""

        @DividedDataField(3, "B")
        var b: String = ""

        @DividedDataField(2, "C")
        var c: String = ""

        @DividedDataField(4, "D")
        var d: String = ""

        @DividedDataField(1, "E")
        var e: String = ""

        @DividedDataField(6, "F")
        var f: String = ""

        @DividedDataField(10, "G")
        var g: Int = 0

        @DividedDataField(8, "H")
        var h: String = ""

        @DividedDataField(9, "I")
        var i: String = ""

        @DividedDataField(7, "J")
        var j: String = ""

        companion object
        {
            val correctOrder = listOf("E", "C", "B", "D", "A", "F", "J", "H", "I", "G")
            val parseData = "E|C|B|D|A|F|J|H|I|0"
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
        val parsedObject: StandardProtocol = pata.deserialize(StandardProtocol.parseData)
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
        assertEquals("|||||||||0", pata.serialize(StandardProtocol()))
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
        @DividedDataField(order = 1, name = "Item Name")
        var itemName: String = "",

        @DividedDataField(order = 2, name ="Price")
        var price: Int = 0,

        @DividedDataField(order = 3, name ="Currency")
        var currency: Currency = Currency.KOREAN_WON,
    ): DividedPataModel("|")

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
}