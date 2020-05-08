package com.dashdevs.qr_nbu.util

import com.dashdevs.qr_nbu.model.PaymentDetails
import com.dashdevs.qr_nbu.model.PaymentDetails.Companion.ENCODING_VALUE
import com.dashdevs.qr_nbu.model.PaymentDetails.Companion.FUNCTION_VALUE
import com.dashdevs.qr_nbu.model.PaymentDetails.Companion.MANDATORY_SERVICE_TAG
import com.dashdevs.qr_nbu.model.PaymentDetails.Companion.UAH_CURRENCY_CODE
import com.dashdevs.qr_nbu.model.PaymentDetailsValidationResult
import com.dashdevs.qr_nbu.model.QrDataError
import java.math.BigDecimal

/**
 * Class to be used for validation of [PaymentDetails] object
 */
internal class PaymentDetailsValidator {

    /**
     * Max possible amount
     */
    private val maxAmount = "999999999.99".toBigDecimal()

    /**
     * A list of supported QR data versions
     */
    private val supportedVersions = arrayListOf(Versions.FIRST)

    /**
     * This method is used to validate [PaymentDetails] object
     *
     * @return [PaymentDetailsValidationResult.Success] if input data is valid
     *   and [PaymentDetailsValidationResult.Error] - otherwise
     */
    fun validateDetails(details: PaymentDetails): PaymentDetailsValidationResult {

        val version = details.version
        if (version in supportedVersions && version == Versions.FIRST) {
            when {
                details.tag != MANDATORY_SERVICE_TAG ->
                    return errorResult(QrDataError.ERROR_INVALID_MANDATORY_SERVICE_TAG)
                details.encoding != ENCODING_VALUE ->
                    return errorResult(QrDataError.ERROR_INVALID_ENCODING_VALUE)
                details.function != FUNCTION_VALUE ->
                    return errorResult(QrDataError.ERROR_INVALID_FUNCTION_VALUE)
                details.bic.isNotEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_BIC_VALUE)
                details.goal.isNotEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_GOAL_VALUE)
                details.reference.isNotEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_REFERENCE_VALUE)
                details.display.isNotEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_DISPLAY_VALUE)
                details.currency != UAH_CURRENCY_CODE ->
                    return errorResult(QrDataError.ERROR_INVALID_CURRENCY_VALUE)
                details.amount != null && (details.amount >= maxAmount || details.amount < BigDecimal.ZERO) ->
                    return errorResult(QrDataError.ERROR_INVALID_AMOUNT)
                details.beneficiary.isEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_BENEFICIARY_VALUE)
                details.beneficiaryAccount.isEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_BENEFICIARY_ACCOUNT_VALUE)
                details.beneficiaryCode.isEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_BENEFICIARY_CODE_VALUE)
                details.paymentPurpose.isEmpty() ->
                    return errorResult(QrDataError.ERROR_INVALID_PAYMENT_PURPOSE_VALUE)
            }
        } else {
            return errorResult(QrDataError.ERROR_NON_SUPPORTED_QR_VERSION)
        }

        return PaymentDetailsValidationResult.Success
    }

    /**
     * This method is used to return [PaymentDetailsValidationResult.Error]
     *
     * @param error A certain error of [PaymentDetails] validation
     */
    private fun errorResult(error: QrDataError) = PaymentDetailsValidationResult.Error(error)

    private object Versions {
        const val FIRST = "001"
    }
}