package io.pemassi.pata.models.converters.deserializers.field

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.pemassi.pata.Pata
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.exceptions.DataFieldNullException
import io.pemassi.pata.models.FixedLengthPataModel

internal class PataDataFieldStringToEnumDeserializerTest: FreeSpec()
{
    init
    {
        val pata = Pata().also {
            it.registerDataFieldDeserializer(PataDataFieldStringToEnumDeserializer())
        }

        data class Test(
            @FixedDataField(1, "TestEnum", 2)
            var test: TestEnum? = null
        ): FixedLengthPataModel<String>(
            checkNullMode = CheckNullMode.EXCEPTION
        )

        "String to enum" - {
            "Success" {
                // Given
                val input = "B "

                // When
                val result: Test = pata.deserialize(input)

                // Then
                result.test shouldBe TestEnum.B
            }

            "Not found" {
                // Given
                val input = "D "

                // When & Then
                shouldThrow<DataFieldNullException> {
                    pata.deserialize<String, Test>(input)
                }
            }
        }
    }
}

enum class TestEnum {
    A, B, C
}
