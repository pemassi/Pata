package io.pemassi.pata

import io.kotest.core.spec.style.FreeSpec
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.extensions.padEndByByteLength
import io.pemassi.pata.models.FixedLengthPataModel
import org.junit.jupiter.api.Assertions.*
import java.nio.charset.Charset
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

internal class PataTest: FreeSpec() {

    private val total = 1_000_000

    init {
        beforeTest {
            // Boost performance
            for(i in 0..total)
            {
                val boost = 1 + 1
            }
        }

        "Speed Test" - {
            "FixedLength" - {

                data class TestFixedLength (
                    @FixedDataField(1, "A", 5)
                    val a: String = "",

                    @FixedDataField(2, "B", 5)
                    val b: String = "",

                    @FixedDataField(3, "C", 5)
                    val c: String = "",

                    @FixedDataField(4, "D", 5)
                    val d: String = "",

                    @FixedDataField(5, "E", 5)
                    val e: String = "",

                    @FixedDataField(6, "F", 5)
                    val f: String = "",

                    @FixedDataField(7, "G", 5)
                    val g: String = "",

                    @FixedDataField(8, "H", 5)
                    val h: String = "",

                    @FixedDataField(9, "I", 5)
                    val i: String = "",

                    @FixedDataField(10, "J", 5)
                    val j: String = "",
                ): FixedLengthPataModel<String>()

                "Serialize" - {
                    val testFixedLength = (0..total).map {
                        TestFixedLength(
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                            getRandomString(5),
                        )

                    }

                    "Manual" - {
                        var minElapsed = Long.MAX_VALUE
                        var highElapsed = Long.MIN_VALUE
                        var totalElapsed = 0L

                        for(i in 0..total)
                        {
                            val elapsed = measureNanoTime {
                                val stringBuilder = StringBuilder(50)
                                stringBuilder.append(testFixedLength[i].a.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].b.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].c.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].d.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].e.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].f.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].g.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].h.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].i.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                stringBuilder.append(testFixedLength[i].j.padEndByByteLength(5, ' ', Charset.defaultCharset()))
                                val serialized = stringBuilder.toString()
                            }
                            totalElapsed += elapsed
                            if(elapsed < minElapsed)
                                minElapsed = elapsed
                            if(elapsed > highElapsed)
                                highElapsed = elapsed
                        }

                        println("Serialize Manual(min = $minElapsed, high = $highElapsed, average = ${totalElapsed / total})")
                        assertTrue(true)
                    }

                    "Pata" - {
                        val pata = Pata()
                        var minElapsed = Long.MAX_VALUE
                        var highElapsed = Long.MIN_VALUE
                        var totalElapsed = 0L

                        for(i in 0..total)
                        {
                            val elapsed = measureNanoTime {
                                val serialized: String = pata.serialize(testFixedLength[i])
                            }
                            totalElapsed += elapsed
                            if(elapsed < minElapsed)
                                minElapsed = elapsed
                            if(elapsed > highElapsed)
                                highElapsed = elapsed
                        }

                        println("Serialize Pata(min = $minElapsed, high = $highElapsed, average = ${totalElapsed / total})")
                        assertTrue(true)
                    }
                }

                "Deserialize" - {
                    val serialized = (0..total).map {
                        getRandomString(50)
                    }

                    "Manual" - {
                        var minElapsed = Long.MAX_VALUE
                        var highElapsed = Long.MIN_VALUE
                        var totalElapsed = 0L

                        for(index in 0..total)
                        {
                            val elapsed = measureNanoTime {
                                val a = serialized[index].substring(0, 5)
                                val b = serialized[index].substring(5, 10)
                                val c = serialized[index].substring(10, 15)
                                val d = serialized[index].substring(15, 20)
                                val e = serialized[index].substring(20, 25)
                                val f = serialized[index].substring(25, 30)
                                val g = serialized[index].substring(30, 35)
                                val h = serialized[index].substring(35, 40)
                                val i = serialized[index].substring(40, 45)
                                val j = serialized[index].substring(45, 50)
                                val testFixedLength = TestFixedLength(a, b, c, d, e, f, g, h, i, j)
                            }
                            totalElapsed += elapsed
                            if(elapsed < minElapsed)
                                minElapsed = elapsed
                            if(elapsed > highElapsed)
                                highElapsed = elapsed
                        }

                        println("Deserialize Manual(min = $minElapsed, high = $highElapsed, average = ${totalElapsed / total})")
                        assertTrue(true)
                    }

                    "Pata w/ oldInstance" - {
                        val pata = Pata()
                        var minElapsed = Long.MAX_VALUE
                        var highElapsed = Long.MIN_VALUE
                        var totalElapsed = 0L

                        for(i in 0..total)
                        {
                            val elapsed = measureNanoTime {
                                val testFixedLength: TestFixedLength = pata.deserialize(serialized[i], oldInstance = TestFixedLength())
                            }
                            totalElapsed += elapsed
                            if(elapsed < minElapsed)
                                minElapsed = elapsed
                            if(elapsed > highElapsed)
                                highElapsed = elapsed
                        }

                        println("Deserialize Pata w/ oldInstance (min = $minElapsed, high = $highElapsed, average = ${totalElapsed / total})")
                        assertTrue(true)
                    }

                    "Pata w/o oldInstance" - {
                        val pata = Pata()
                        var minElapsed = Long.MAX_VALUE
                        var highElapsed = Long.MIN_VALUE
                        var totalElapsed = 0L

                        for(i in 0..total)
                        {
                            val elapsed = measureNanoTime {
                                val testFixedLength: TestFixedLength = pata.deserialize(serialized[i])
                            }
                            totalElapsed += elapsed
                            if(elapsed < minElapsed)
                                minElapsed = elapsed
                            if(elapsed > highElapsed)
                                highElapsed = elapsed
                        }

                        println("Deserialize Pata w/o oldInstance (min = $minElapsed, high = $highElapsed, average = ${totalElapsed / total})")
                        assertTrue(true)
                    }
                }
            }
        }

    }
}

fun getRandomString(length: Int) : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun main()
{
    data class TestFixedLength (
        @FixedDataField(1, "A", 5)
        val a: String = "",

        @FixedDataField(2, "B", 5)
        val b: String = "",

        @FixedDataField(3, "C", 5)
        val c: String = "",

        @FixedDataField(4, "D", 5)
        val d: String = "",

        @FixedDataField(5, "E", 5)
        val e: String = "",

        @FixedDataField(6, "F", 5)
        val f: String = "",

        @FixedDataField(7, "G", 5)
        val g: String = "",

        @FixedDataField(8, "H", 5)
        val h: String = "",

        @FixedDataField(9, "I", 5)
        val i: String = "",

        @FixedDataField(10, "J", 5)
        val j: String = "",
    ): FixedLengthPataModel<String>()

    val pata = Pata()
    var minElapsed = Long.MAX_VALUE
    var highElapsed = Long.MIN_VALUE
    var totalElapsed = 0L

    for(i in 0..10)
    {
        val elapsed = measureNanoTime {
            for(i in 0..100000)
            {
                val testFixedLength: TestFixedLength = pata.deserialize("A    B    C    D    E    F    G    H    I    J    ")
            }
        }
        totalElapsed += elapsed
        if(elapsed < minElapsed)
            minElapsed = elapsed
        if(elapsed > highElapsed)
            highElapsed = elapsed
    }

    println("Pata(min = $minElapsed, high = $highElapsed, average = ${totalElapsed / 10})")
}

//209442524
//160330720

//Manual(min = 167, high = 33361583, average = 645)
//Pata(min = 41, high = 1462625, average = 393)
//Manual(min = 41, high = 875542, average = 309)
//Pata(min = 1083, high = 39273375, average = 1757)
