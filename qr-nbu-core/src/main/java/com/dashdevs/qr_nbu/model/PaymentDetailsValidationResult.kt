package com.dashdevs.qr_nbu.model

/**
 * A sealed class which represents a result of [PaymentDetails] validation
 */
sealed class PaymentDetailsValidationResult {
    object Success : PaymentDetailsValidationResult()
    data class Error(val error: QrDataError) : PaymentDetailsValidationResult()
}