package com.dashdevs.qr_nbu.callback

import com.dashdevs.qr_nbu.model.QrResult

/**
 * Interface definition for a callback to be invoked when asynchronous
 * operation for decoding, encoding or generating QR code return result [com.dashdevs.qr_nbu.model.QrResult]
 *
 * @param R the type of return result @see[com.dashdevs.qr_nbu.model.QrResult]
 */
interface QrResultCallback<R : QrResult> {
    /**
     * Called when asynchronous operation for decoding, encoding or generating QR code is done
     * @param result A return result [com.dashdevs.qr_nbu.model.QrResult]
     */
    fun onResult(result: R)
}