package com.dashdevs.qr_nbu.manager

import android.content.Intent
import androidx.annotation.Px
import androidx.fragment.app.FragmentActivity
import com.dashdevs.qr_nbu.callback.QrResultCallback
import com.dashdevs.qr_nbu.model.PaymentDataToEncode
import com.dashdevs.qr_nbu.model.QrResult.DecodeResult
import com.dashdevs.qr_nbu.model.QrResult.EncodeResult
import com.dashdevs.qr_nbu.model.QrResult.GenerateQrResult
import com.dashdevs.qr_nbu.model.ScanResult

/**
 * A constant that defines the default width and height (in pixels) of generated QR code bitmap
 */
@Px
private const val DEFAULT_QR_SIZE = 400

/**
 * Interface to be extended by [QrCodeManagerImpl]
 *
 * This interface is used for encoding and decoding QR data, generating QR code
 * according to National Bank of Ukraine QR structure standard
 */
interface QrCodeManager {
    /**
     * This method is used to start QR scanning process
     *
     * @param activity A current activity [FragmentActivity] to initiate scan
     */
    fun startScan(activity: FragmentActivity)

    /**
     * This method is used to decode QR string to [com.dashdevs.qr_nbu.model.PaymentDetails] object,
     * or return an error [com.dashdevs.qr_nbu.model.QrDataError] and wrap the return value to [DecodeResult]
     *
     * @param data A raw string data of Qr code
     * @return The result of decoding [DecodeResult]
     */
    fun decodeQrData(data: String): DecodeResult

    /**
     * This method is used to decode QR string asynchronously
     *
     * @see[decodeQrData]
     *
     * @param onResult A lambda expression to handle result [DecodeResult]
     */
    fun decodeQrData(data: String, onResult: (DecodeResult) -> Unit)

    /**
     * This method is used to decode QR string asynchronously
     *
     * @see[decodeQrData]
     *
     * @param callback A callback to handle result [DecodeResult]
     */
    fun decodeQrData(data: String, callback: QrResultCallback<DecodeResult>)

    /**
     * This method is used to encode mandatory payment data to [String] data object,
     * or return an error [com.dashdevs.qr_nbu.model.QrDataError] and wrap the return value to [EncodeResult]
     *
     * @param paymentData An object [PaymentDataToEncode] that encapsulate mandatory payment information
     * @return The result of encoding [EncodeResult]
     */
    fun encodeQrData(paymentData: PaymentDataToEncode): EncodeResult

    /**
     * This method is used to encode mandatory payment data to [String] data object asynchronously
     *
     * @see[encodeQrData]
     *
     * @param onResult A lambda expression to handle result [EncodeResult]
     */
    fun encodeQrData(paymentData: PaymentDataToEncode, onResult: (EncodeResult) -> Unit)

    /**
     * This method is used to encode mandatory payment data to [String] data object asynchronously
     *
     * @see[encodeQrData]
     *
     * @param callback A callback to handle result [EncodeResult]
     */
    fun encodeQrData(paymentData: PaymentDataToEncode, callback: QrResultCallback<EncodeResult>)

    /**
     * This method is used to handle Qr data after scanning process
     *
     * @param requestCode from [android.app.Activity.onActivityResult]
     * @param resultCode from [android.app.Activity.onActivityResult]
     * @param data from [android.app.Activity.onActivityResult]
     *
     * @return An object of [ScanResult], which contains Qr data string content
     */
    fun handleScanResult(requestCode: Int, resultCode: Int, data: Intent?): ScanResult?

    /**
     * This method is used to validate mandatory payment data and generate QR bitmap[android.graphics.Bitmap]
     * from [PaymentDataToEncode] object or return an error [com.dashdevs.qr_nbu.model.QrDataError] and wrap
     * the return value to [GenerateQrResult]
     *
     * @param paymentData An object [PaymentDataToEncode] that encapsulate mandatory payment information
     * @param qrWidth A width (in pixels) of QR code bitmap
     * @param qrHeight A height (in pixels) of QR code bitmap
     * @return The result[GenerateQrResult] of QR generating
     */
    fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int = DEFAULT_QR_SIZE,
        @Px qrHeight: Int = DEFAULT_QR_SIZE
    ): GenerateQrResult

    /**
     * This method is used to generate QR bitmap[android.graphics.Bitmap] asynchronously
     *
     * @see[generateQr]
     *
     * @param onResult A lambda expression to handle result [GenerateQrResult]
     */
    fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int = DEFAULT_QR_SIZE,
        @Px qrHeight: Int = DEFAULT_QR_SIZE,
        onResult: (GenerateQrResult) -> Unit
    )

    /**
     * This method is used to generate QR bitmap[android.graphics.Bitmap] asynchronously
     *
     * @see[generateQr]
     *
     * @param callback A callback to handle result [GenerateQrResult]
     */
    fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int = DEFAULT_QR_SIZE,
        @Px qrHeight: Int = DEFAULT_QR_SIZE,
        callback: QrResultCallback<GenerateQrResult>
    )
}