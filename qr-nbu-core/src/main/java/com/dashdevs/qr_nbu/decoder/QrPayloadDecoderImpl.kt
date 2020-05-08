package com.dashdevs.qr_nbu.decoder

import com.dashdevs.qr_nbu.model.PaymentDetails
import com.dashdevs.qr_nbu.model.PaymentDetailsValidationResult
import com.dashdevs.qr_nbu.model.QrDataError
import com.dashdevs.qr_nbu.model.QrResult
import com.dashdevs.qr_nbu.util.PaymentDetailsValidator
import com.dashdevs.qr_nbu.util.sizeIsNotValid

/**
 * Class to be used for decoding qr data according to National Bank of Ukraine QR structure standard
 */
internal class QrPayloadDecoderImpl : QrPayloadDecoder {

    /**
     * A range of possible elements in QR data payload
     */
    private val elementsRange = IntRange(12, 14)

    /**
     * A validator to be used for validation of [PaymentDetails] object
     *
     * @see[PaymentDetailsValidator]
     */
    private val paymentDetailsValidator by lazy { PaymentDetailsValidator() }

    override fun decodeQRData(data: String): QrResult.DecodeResult {
        when {
            data.isEmpty() -> return errorResult(QrDataError.ERROR_EMPTY_DATA)
            data.sizeIsNotValid() -> return errorResult(QrDataError.ERROR_INVALID_INPUT_SIZE)
            data.elementsNumberIsNotValid() -> return errorResult(QrDataError.ERROR_INVALID_ELEMENTS_SIZE)
        }

        val details = parseQRPayloadToPaymentDetails(data)
        when (val detailsResult = paymentDetailsValidator.validateDetails(details)) {
            is PaymentDetailsValidationResult.Error -> return errorResult(detailsResult.error)
        }

        return QrResult.DecodeResult.Success(details)
    }

    /**
     * This method is used to determine whether the elements number
     * of Qr data payload is correct or not
     *
     * @return true if elements number is not valid, false otherwise
     */
    private fun String.elementsNumberIsNotValid() = lines().size !in elementsRange

    /**
     * This method is used to parse raw Qr data string to [PaymentDetails] object
     *
     * @param data A valid string data of Qr code
     * @return [PaymentDetails] object as a parsing result
     */
    private fun parseQRPayloadToPaymentDetails(data: String): PaymentDetails {
        val elements = data.lines()
        val currencyAmount = elements[ElementIndex.CURRENCY_AMOUNT]
        return PaymentDetails(
            elements[ElementIndex.SERVICE_TAG],
            elements[ElementIndex.VERSION],
            elements[ElementIndex.ENCODING],
            elements[ElementIndex.FUNCTION],
            elements[ElementIndex.BIC],
            elements[ElementIndex.BENEFICIARY],
            elements[ElementIndex.BENEFICIARY_ACCOUNT],
            currencyAmount.take(3),
            currencyAmount.substring(3, currencyAmount.length).toBigDecimalOrNull(),
            elements[ElementIndex.BENEFICIARY_CODE],
            elements[ElementIndex.GOAL],
            elements[ElementIndex.REFERENCE],
            elements[ElementIndex.PAYMENT_PURPOSE],
            elements.getOrNull(ElementIndex.DISPLAY) ?: ""
        )
    }

    /**
     * This method is used to return [QrResult.DecodeResult.Error]
     *
     * @param error A certain error of QR data validation
     */
    private fun errorResult(error: QrDataError) = QrResult.DecodeResult.Error(error)

    /**
     * An object with Qr data elements indexes.
     * QR code elements should be arranged in a proper sequence
     */
    private object ElementIndex {
        const val SERVICE_TAG = 0
        const val VERSION = 1
        const val ENCODING = 2
        const val FUNCTION = 3
        const val BIC = 4
        const val BENEFICIARY = 5
        const val BENEFICIARY_ACCOUNT = 6
        const val CURRENCY_AMOUNT = 7
        const val BENEFICIARY_CODE = 8
        const val GOAL = 9
        const val REFERENCE = 10
        const val PAYMENT_PURPOSE = 11
        const val DISPLAY = 12
    }
}