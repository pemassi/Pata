/*
 * Copyright (c) 2021 Kyungyoon Kim.
 * All rights reserved.
 */

package io.pemassi.datamodelbuilder

import io.pemassi.datamodelbuilder.annotations.FixedDataField
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class FixedLengthDataModelTest
{
    val EUC_KR = Charset.forName("EUC_KR")

    class TestProtocol : FixedLengthDataModel()
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
    }

    val correctOrder = listOf("E", "C", "B", "D", "A", "F", "J", "H", "I", "G")
    val parseData = "E    C    B    D    A    F    J    H    I    00001"

    @Test
    fun `Order Protocol In Order`()
    {
        //When created
        val createdObject = TestProtocol()
        assertArrayEquals(
            correctOrder.toTypedArray(),
            createdObject.propertyDatabase.map { it.second.name }.toTypedArray()
        )

        //When parsed
        val parsedObject = FixedLengthDataModel.parse<TestProtocol>(parseData)
        assertArrayEquals(
            correctOrder.toTypedArray(),
            parsedObject.propertyDatabase.map { it.second.name }.toTypedArray()
        )
    }

    @Test
    fun `Parse correctly`()
    {
        //Test parsed data
        val parsedObject = FixedLengthDataModel.parse<TestProtocol>(parseData)
        assertEquals(parsedObject.a.trim(), "A")
        assertEquals(parsedObject.b.trim(), "B")
        assertEquals(parsedObject.c.trim(), "C")
        assertEquals(parsedObject.d.trim(), "D")
        assertEquals(parsedObject.e.trim(), "E")
        assertEquals(parsedObject.f.trim(), "F")
        assertEquals(parsedObject.g, 1)
        assertEquals(parsedObject.h.trim(), "H")
        assertEquals(parsedObject.i.trim(), "I")
        assertEquals(parsedObject.j.trim(), "J")

        //Test data is same as parsed data
        assertEquals(parsedObject.toString(), parseData)
    }

    @Test
    fun `Korean Test`()
    {
        val korean = "한글"
        val created = TestProtocol().also {
            it.a = korean
        }

        val parsed = FixedLengthDataModel.parse<TestProtocol>(created.toString(EUC_KR), EUC_KR)

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

        assertEquals(created.toString(EUC_KR), created.toString(EUC_KR))

    }
}