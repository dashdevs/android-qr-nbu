package com.dashdevs.qr_nbu

import com.dashdevs.qr_nbu.encoder.QrPayloadEncoder
import com.dashdevs.qr_nbu.encoder.QrPayloadEncoderImpl
import com.dashdevs.qr_nbu.model.QrDataError
import com.dashdevs.qr_nbu.model.QrResult
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.math.BigDecimal

@RunWith(Parameterized::class)
class QrCodeEncoderTest(private val arguments: TestArguments) {

    private val encoder: QrPayloadEncoder = QrPayloadEncoderImpl()

    @Test
    fun `given mandatory fields when decoder decodes data then return the correct result`() {
        // Given
        val qrData = arguments.qrData

        // When
        val result = encoder.encodeQRData(
            qrData.beneficiary,
            qrData.beneficiaryAccount,
            qrData.amount,
            qrData.beneficiaryCode,
            qrData.paymentPurpose
        )

        // Then
        assertEquals(arguments.expected, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters() = listOf(
            TestArguments(
                DataToEncode("", "", BigDecimal.ZERO, "", ""),
                error(QrDataError.ERROR_INVALID_BENEFICIARY_VALUE)
            ),
            TestArguments(
                DataToEncode(ValidField.BENEFICIARY, "", BigDecimal.ZERO, "", ""),
                error(QrDataError.ERROR_INVALID_BENEFICIARY_ACCOUNT_VALUE)
            ),
            TestArguments(
                DataToEncode(
                    ValidField.BENEFICIARY,
                    ValidField.BENEFICIARY_ACCOUNT,
                    BigDecimal.ZERO,
                    "",
                    ""
                ),
                error(QrDataError.ERROR_INVALID_BENEFICIARY_CODE_VALUE)
            ),
            TestArguments(
                DataToEncode(
                    ValidField.BENEFICIARY,
                    ValidField.BENEFICIARY_ACCOUNT,
                    BigDecimal.ZERO,
                    ValidField.BENEFICIARY_CODE,
                    ""
                ),
                error(QrDataError.ERROR_INVALID_PAYMENT_PURPOSE_VALUE)
            ),
            TestArguments(
                DataToEncode(
                    ValidField.BENEFICIARY,
                    ValidField.BENEFICIARY_ACCOUNT,
                    ValidField.AMOUNT.toBigDecimal(),
                    ValidField.BENEFICIARY_CODE,
                    ValidField.PAYMENT_PURPOSE
                ),
                QrResult.EncodeResult.Success(validData)
            )
        )

        private fun error(error: QrDataError) = QrResult.EncodeResult.Error(error)
    }

    data class TestArguments(
        val qrData: DataToEncode,
        val expected: Any
    )

    data class DataToEncode(
        val beneficiary: String,
        val beneficiaryAccount: String,
        val amount: BigDecimal,
        val beneficiaryCode: String,
        val paymentPurpose: String
    )
}
