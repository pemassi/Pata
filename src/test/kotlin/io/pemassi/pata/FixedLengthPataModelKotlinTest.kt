/*
 * Copyright (c) 2021 Kyungyoon Kim(pemassi).
 * All rights reserved.
 */

package io.pemassi.pata

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.enums.PaddingMode
import io.pemassi.pata.enums.ReplaceNullMode
import io.pemassi.pata.enums.TrimMode
import io.pemassi.pata.exceptions.DataFieldNullException
import io.pemassi.pata.interfaces.PataDataFieldSerializer
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.converters.serializers.field.PataDataFieldStringToStringSerializer
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

internal class FixedLengthPataModelKotlinTest: FreeSpec()
{
    init
    {
        val pata = Pata()

        "If nullable data field exists" - {
            "When deserializing" - {
                "If data is null" - {
                    "When" - {
                        "KEEP" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                replaceNullMode = ReplaceNullMode.KEEP
                            )
                            val data = "     "
                            val deserialized: TestProtocol = pata.deserialize(data)

                            deserialized.a shouldBe ""
                        }

                        "REPLACE" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                replaceNullMode = ReplaceNullMode.WHEN_BLANK,
                                trimMode = TrimMode.KEEP,
                            )
                            val data = "     "
                            val deserialized: TestProtocol = pata.deserialize(data)

                            deserialized.a shouldBe null
                        }

                        "REPLACE" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                replaceNullMode = ReplaceNullMode.WHEN_EMPTY,
                                trimMode = TrimMode.BOTH_TRIM,
                            )
                            val data = "     "
                            val deserialized: TestProtocol = pata.deserialize(data)

                            deserialized.a shouldBe null
                        }
                    }


                    "When null check mode is" - {
                        "KEEP" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                checkNullMode = CheckNullMode.KEEP
                            )
                            val data = "     "
                            val deserialized: TestProtocol = pata.deserialize(data)

                            deserialized.a shouldBe ""
                        }

                        "REPLACE" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                checkNullMode = CheckNullMode.REPLACE,
                                replaceNullMode = ReplaceNullMode.WHEN_BLANK
                            )
                            val data = "     "
                            val deserialized: TestProtocol = pata.deserialize(data)

                            deserialized.a shouldBe ""
                        }

                        "EXCEPTION" {
                            data class TestProtocol
                                (
                                @FixedDataField(1, "A", 5)
                                var a: String? = null,
                            ): FixedLengthPataModel<String>(
                                checkNullMode = CheckNullMode.EXCEPTION
                            )
                            val data = "     "

                            shouldThrow<DataFieldNullException> {
                                val deserialized: TestProtocol = pata.deserialize(data)
                                deserialized
                            }
                        }
                    }
                }
            }
        }



    }
}