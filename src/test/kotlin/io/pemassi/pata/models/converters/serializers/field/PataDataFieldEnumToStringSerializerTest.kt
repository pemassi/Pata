package io.pemassi.pata.models.converters.serializers.field

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.pemassi.pata.Pata
import io.pemassi.pata.annotations.FixedDataField
import io.pemassi.pata.enums.CheckNullMode
import io.pemassi.pata.exceptions.DataFieldNullException
import io.pemassi.pata.models.FixedLengthPataModel
import io.pemassi.pata.models.converters.deserializers.field.PataDataFieldStringToEnumDeserializer
import org.junit.jupiter.api.Assertions.*

internal class PataDataFieldEnumToStringSerializerTest: FreeSpec()
{
    init
    {
        val pata = Pata().also {
            it.registerDataFieldSerializer(PataDataFieldEnumToStringSerializer())
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
                val input = Test(TestEnum.B)

                // When
                val result: String = pata.serialize(input)

                // Then
                result shouldBe "B "
            }
        }
    }
}

enum class TestEnum {
    A, B, C
}

