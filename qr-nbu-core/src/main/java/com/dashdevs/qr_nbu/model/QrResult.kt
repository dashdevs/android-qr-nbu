package com.dashdevs.qr_nbu.model

import android.graphics.Bitmap

/**
 * A sealed class which represents a result of decoding QR data, encoding mandatory payment
 * details or generating QR bitmap
 */
sealed class QrResult {

    /**
     * A sealed class which represents a result of decoding QR data
     */
    sealed class DecodeResult : QrResult() {
        data class Success(val details: PaymentDetails) : DecodeResult()
        data class Error(val error: QrDataError) : DecodeResult()
    }

    /**
     * A sealed class which represents a result of encoding mandatory payment details
     */
    sealed class EncodeResult : QrResult() {
        data class Success(val data: String) : EncodeResult()
        data class Error(val error: QrDataError) : EncodeResult()
    }

    /**
     * A sealed class which represents a result of generating QR bitmap
     */
    sealed class GenerateQrResult : QrResult() {
        data class Success(val qr: Bitmap) : GenerateQrResult()
        data class Error(val error: QrDataError) : GenerateQrResult()
    }
}