package com.dashdevs.qr_nbu

import com.dashdevs.qr_nbu.decoder.QrPayloadDecoder
import com.dashdevs.qr_nbu.decoder.QrPayloadDecoderImpl
import com.dashdevs.qr_nbu.model.QrDataError
import com.dashdevs.qr_nbu.model.QrResult
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

private const val INVALID_VALUE = "INVALID"

@RunWith(Parameterized::class)
class QRCodeDecoderTest(
    private val arguments: TestArguments
) {

    private val decoder: QrPayloadDecoder = QrPayloadDecoderImpl()

    @Test
    fun `given qr data when decoder decodes data then return the correct result`() {
        // Given
        val qrData = arguments.qrData

        // When
        val result = decoder.decodeQRData(qrData)

        // Then
        assertEquals(arguments.expected, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun parameters() =
            listOf(
                TestArguments(
                    QRData.EMPTY_DATA,
                    error(QrDataError.ERROR_EMPTY_DATA)
                ),
                TestArguments(
                    QRData.INVALID_DATA_SIZE,
                    error(QrDataError.ERROR_INVALID_INPUT_SIZE)
                ),
                TestArguments(
                    QRData.UNKNOWN_DATA,
                    error(QrDataError.ERROR_INVALID_ELEMENTS_SIZE)
                ),
                TestArguments(
                    QRData.INVALID_QR_ELEMENTS_NUMBER,
                    error(QrDataError.ERROR_INVALID_ELEMENTS_SIZE)
                ),
                TestArguments(
                    QRData.INVALID_MANDATORY_SERVICE_TAG,
                    error(QrDataError.ERROR_INVALID_MANDATORY_SERVICE_TAG)
                ),
                TestArguments(
                    QRData.INVALID_ENCODING_VALUE,
                    error(QrDataError.ERROR_INVALID_ENCODING_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_FUNCTION_VALUE,
                    error(QrDataError.ERROR_INVALID_FUNCTION_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_BIC_VALUE,
                    error(QrDataError.ERROR_INVALID_BIC_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_GOAL_VALUE,
                    error(QrDataError.ERROR_INVALID_GOAL_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_REFERENCE_VALUE,
                    error(QrDataError.ERROR_INVALID_REFERENCE_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_DISPLAY_VALUE,
                    error(QrDataError.ERROR_INVALID_DISPLAY_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_CURRENCY_CODE,
                    error(QrDataError.ERROR_INVALID_CURRENCY_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_AMOUNT_LESS_THAN_ZERO,
                    error(QrDataError.ERROR_INVALID_AMOUNT)
                ),
                TestArguments(
                    QRData.INVALID_AMOUNT_MORE_THAN_MAX,
                    error(QrDataError.ERROR_INVALID_AMOUNT)
                ),
                TestArguments(
                    QRData.INVALID_BENEFICIARY,
                    error(QrDataError.ERROR_INVALID_BENEFICIARY_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_BENEFICIARY_ACCOUNT,
                    error(QrDataError.ERROR_INVALID_BENEFICIARY_ACCOUNT_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_BENEFICIARY_CODE,
                    error(QrDataError.ERROR_INVALID_BENEFICIARY_CODE_VALUE)
                ),
                TestArguments(
                    QRData.INVALID_PAYMENT_PURPOSE,
                    error(QrDataError.ERROR_INVALID_PAYMENT_PURPOSE_VALUE)
                ),
                TestArguments(
                    validData,
                    QrResult.DecodeResult.Success(validPaymentDetails)
                )
            )

        private fun error(error: QrDataError) = QrResult.DecodeResult.Error(error)
    }

    data class TestArguments(
        val qrData: String,
        val expected: Any
    )

    private object QRData {

        private fun getPaymentDetailsMapWithInvalidValue(key: String) =
            validPaymentDetailsMap
                .setInvalidValue(key)
                .joinValuesWithSeparator()

        private fun getPaymentDetailsMapWithEmptyValue(key: String) =
            validPaymentDetailsMap
                .setEmptyValue(key)
                .joinValuesWithSeparator()

        private fun HashMap<String, String>.setInvalidValue(key: String): HashMap<String, String> {
            set(key, INVALID_VALUE)
            return this
        }

        private fun HashMap<String, String>.setEmptyValue(key: String): HashMap<String, String> {
            set(key, EMPTY_DATA)
            return this
        }

        const val EMPTY_DATA = ""
        const val UNKNOWN_DATA = "Unknown Data"
        val INVALID_DATA_SIZE = ByteArray(332).contentToString()
        val INVALID_QR_ELEMENTS_NUMBER =
            arrayListOf(
                ValidField.SERVICE_TAG,
                ValidField.VERSION,
                ValidField.ENCODING,
                ValidField.FUNCTION
            ).joinToString(separator = ELEMENT_SEPARATOR, postfix = ELEMENT_SEPARATOR)

        val INVALID_MANDATORY_SERVICE_TAG =
            getPaymentDetailsMapWithInvalidValue(FieldKey.SERVICE_TAG)

        val INVALID_ENCODING_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.ENCODING)

        val INVALID_FUNCTION_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.FUNCTION)

        val INVALID_BIC_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.BIC)

        val INVALID_GOAL_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.GOAL)

        val INVALID_REFERENCE_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.REFERENCE)

        val INVALID_DISPLAY_VALUE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.DISPLAY)

        val INVALID_CURRENCY_CODE =
            getPaymentDetailsMapWithInvalidValue(FieldKey.CURRENCY_AMOUNT)

        val INVALID_AMOUNT_LESS_THAN_ZERO =
            validPaymentDetailsMap
                .apply { set(FieldKey.CURRENCY_AMOUNT, "UAH-1") }
                .joinValuesWithSeparator()

        val INVALID_AMOUNT_MORE_THAN_MAX =
            validPaymentDetailsMap
                .apply { set(FieldKey.CURRENCY_AMOUNT, "UAH1000000000.00") }
                .joinValuesWithSeparator()

        val INVALID_BENEFICIARY =
            getPaymentDetailsMapWithEmptyValue(FieldKey.BENEFICIARY)

        val INVALID_BENEFICIARY_ACCOUNT =
            getPaymentDetailsMapWithEmptyValue(FieldKey.BENEFICIARY_ACCOUNT)

        val INVALID_BENEFICIARY_CODE =
            getPaymentDetailsMapWithEmptyValue(FieldKey.BENEFICIARY_CODE)

        val INVALID_PAYMENT_PURPOSE =
            getPaymentDetailsMapWithEmptyValue(FieldKey.PAYMENT_PURPOSE)
    }
}
