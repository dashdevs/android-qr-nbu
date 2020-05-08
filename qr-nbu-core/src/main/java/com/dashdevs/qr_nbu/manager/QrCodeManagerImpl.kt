package com.dashdevs.qr_nbu.manager

import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.Px
import androidx.fragment.app.FragmentActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.dashdevs.qr_nbu.callback.QrResultCallback
import com.dashdevs.qr_nbu.decoder.QrPayloadDecoder
import com.dashdevs.qr_nbu.decoder.QrPayloadDecoderImpl
import com.dashdevs.qr_nbu.encoder.QrPayloadEncoder
import com.dashdevs.qr_nbu.encoder.QrPayloadEncoderImpl
import com.dashdevs.qr_nbu.manager.QrCodeManagerImpl.Companion.newInstance
import com.dashdevs.qr_nbu.model.PaymentDataToEncode
import com.dashdevs.qr_nbu.model.QrResult.DecodeResult
import com.dashdevs.qr_nbu.model.QrResult.EncodeResult
import com.dashdevs.qr_nbu.model.QrResult.GenerateQrResult
import com.dashdevs.qr_nbu.model.ScanResult
import com.dashdevs.qr_nbu.util.ThreadExecutor

/**
 * Main Class to be used for encoding and decoding QR data, generating QR code
 * according to National Bank of Ukraine QR structure standard
 *
 * @constructor Class has a private constructor in order to restrict an access to it from outside the sdk
 *
 * @see [newInstance]
 */
internal class QrCodeManagerImpl private constructor() : QrCodeManager {
    /**
     * A decoder to be used for decoding qr data
     *
     * @see[QrPayloadDecoder]
     */
    private val decoder: QrPayloadDecoder by lazy { QrPayloadDecoderImpl() }

    /**
     * An encoder to be used for encoding mandatory payment data to Qr code data
     *
     * @see[QrPayloadEncoder]
     */
    private val encoder: QrPayloadEncoder by lazy { QrPayloadEncoderImpl() }

    /**
     * A helper for performing asynchronous operations
     *
     * @see[ThreadExecutor]
     */
    private val executor by lazy { ThreadExecutor() }

    /**
     * The main instance responsible for QR scanning, and QR scan result handling
     *
     * @see[IntentIntegrator]
     */
    private lateinit var intentIntegrator: IntentIntegrator

    override fun startScan(activity: FragmentActivity) {
        if (!::intentIntegrator.isInitialized) {
            intentIntegrator = IntentIntegrator(activity)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setOrientationLocked(true)
        }
        intentIntegrator.initiateScan()
    }

    override fun decodeQrData(data: String) = decoder.decodeQRData(data)

    override fun decodeQrData(data: String, onResult: (DecodeResult) -> Unit) {
        executor.execute(
            { decoder.decodeQRData(data) },
            { onResult(it) }
        )
    }

    override fun decodeQrData(data: String, callback: QrResultCallback<DecodeResult>) {
        executor.execute(
            { decoder.decodeQRData(data) },
            { callback.onResult(it) }
        )
    }

    override fun encodeQrData(paymentData: PaymentDataToEncode) =
        encoder.encodeQRData(
            paymentData.beneficiary,
            paymentData.beneficiaryAccount,
            paymentData.amount,
            paymentData.beneficiaryCode,
            paymentData.paymentPurpose
        )

    override fun encodeQrData(paymentData: PaymentDataToEncode, onResult: (EncodeResult) -> Unit) {
        executor.execute(
            {
                encoder.encodeQRData(
                    paymentData.beneficiary,
                    paymentData.beneficiaryAccount,
                    paymentData.amount,
                    paymentData.beneficiaryCode,
                    paymentData.paymentPurpose
                )
            },
            { onResult(it) }
        )
    }

    override fun encodeQrData(
        paymentData: PaymentDataToEncode,
        callback: QrResultCallback<EncodeResult>
    ) {
        executor.execute(
            {
                encoder.encodeQRData(
                    paymentData.beneficiary,
                    paymentData.beneficiaryAccount,
                    paymentData.amount,
                    paymentData.beneficiaryCode,
                    paymentData.paymentPurpose
                )
            },
            { callback.onResult(it) }
        )
    }

    override fun handleScanResult(requestCode: Int, resultCode: Int, data: Intent?): ScanResult? {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        return if (intentResult != null) {
            ScanResult(intentResult.contents)
        } else null
    }

    override fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int,
        @Px qrHeight: Int
    ): GenerateQrResult =
        when (val result = encodeQrData(paymentData)) {
            is EncodeResult.Success ->
                GenerateQrResult.Success(getQrBitmap(result.data, qrWidth, qrHeight))
            is EncodeResult.Error -> GenerateQrResult.Error(result.error)
        }

    @Throws(Exception::class)
    override fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int,
        @Px qrHeight: Int,
        onResult: (GenerateQrResult) -> Unit
    ) {
        executor.execute(
            {
                when (val result = encodeQrData(paymentData)) {
                    is EncodeResult.Success ->
                        GenerateQrResult.Success(getQrBitmap(result.data, qrWidth, qrHeight))
                    is EncodeResult.Error -> GenerateQrResult.Error(result.error)
                }
            },
            { onResult(it) }
        )
    }

    @Throws(Exception::class)
    override fun generateQr(
        paymentData: PaymentDataToEncode,
        @Px qrWidth: Int,
        @Px qrHeight: Int,
        callback: QrResultCallback<GenerateQrResult>
    ) {
        executor.execute(
            {
                when (val result = encodeQrData(paymentData)) {
                    is EncodeResult.Success ->
                        GenerateQrResult.Success(getQrBitmap(result.data, qrWidth, qrHeight))
                    is EncodeResult.Error -> GenerateQrResult.Error(result.error)
                }
            },
            { callback.onResult(it) }
        )
    }

    /**
     * This method is used to generate QR bitmap[android.graphics.Bitmap]
     *
     * @see[generateQr] for variables definitions
     *
     * @return A generated QR code bitmap[android.graphics.Bitmap]
     * @exception [Exception]
     */
    @Throws(Exception::class)
    private fun getQrBitmap(data: String, width: Int, height: Int): Bitmap =
        BarcodeEncoder().encodeBitmap(
            data,
            BarcodeFormat.QR_CODE,
            width,
            height,
            mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        )

    companion object {
        /**
         * Static method to provide an instance of [QrCodeManager]
         *
         * @return An instance of [QrCodeManager]
         *
         * @see[QrCodeManager]
         */
        fun newInstance(): QrCodeManager = QrCodeManagerImpl()
    }
}