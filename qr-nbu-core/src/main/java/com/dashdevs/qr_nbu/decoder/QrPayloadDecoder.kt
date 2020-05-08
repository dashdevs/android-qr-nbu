package com.dashdevs.qr_nbu.decoder

import com.dashdevs.qr_nbu.model.QrResult

/**
 * Interface to be extended by [QrPayloadDecoderImpl]
 *
 * This interface is used for decoding qr data according to National Bank of Ukraine QR structure standard
 */
internal interface QrPayloadDecoder {
    /**
     * This method is used to decode QR string to [com.dashdevs.qr_nbu.model.PaymentDetails] object,
     * or return an error [com.dashdevs.qr_nbu.model.QrDataError] and wrap the return value to [QrResult.DecodeResult]
     *
     * @param data A raw string data of Qr code
     * @return The result of decoding [QrResult.DecodeResult]
     */
    fun decodeQRData(data: String): QrResult.DecodeResult
}