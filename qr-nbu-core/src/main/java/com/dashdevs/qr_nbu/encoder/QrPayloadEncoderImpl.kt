package com.dashdevs.qr_nbu.encoder

import com.dashdevs.qr_nbu.model.PaymentDetails
import com.dashdevs.qr_nbu.model.PaymentDetailsValidationResult
import com.dashdevs.qr_nbu.model.QrDataError
import com.dashdevs.qr_nbu.model.QrResult
import com.dashdevs.qr_nbu.util.PaymentDetailsValidator
import com.dashdevs.qr_nbu.util.sizeIsNotValid
import java.math.BigDecimal

/**
 * Class to be used for encoding mandatory payment data to Qr code data
 * according to National Bank of Ukraine QR structure standard
 */
internal class QrPayloadEncoderImpl : QrPayloadEncoder {

    /**
     * A validator to be used for validation of [PaymentDetails] object
     *
     * @see[PaymentDetailsValidator]
     */
    private val paymentDetailsValidator by lazy { PaymentDetailsValidator() }

    override fun encodeQRData(
        beneficiary: String,
        beneficiaryAccount: String,
        amount: BigDecimal,
        beneficiaryCode: String,
        paymentPurpose: String
    ): QrResult.EncodeResult {
        val details = PaymentDetails(
            beneficiary = beneficiary.trim(),
            beneficiaryAccount = beneficiaryAccount.trim(),
            amount = amount,
            beneficiaryCode = beneficiaryCode.trim(),
            paymentPurpose = paymentPurpose.trim()
        )

        val qrData = details.getQrData()
        if (qrData.sizeIsNotValid()) {
            return QrResult.EncodeResult.Error(QrDataError.ERROR_INVALID_INPUT_SIZE)
        }

        return when (val detailsResult = paymentDetailsValidator.validateDetails(details)) {
            is PaymentDetailsValidationResult.Success -> QrResult.EncodeResult.Success(qrData)
            is PaymentDetailsValidationResult.Error -> QrResult.EncodeResult.Error(detailsResult.error)
        }
    }
}