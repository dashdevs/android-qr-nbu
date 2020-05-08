package com.dashdevs.qr_nbu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dashdevs.qr_nbu.decoder.QrPayloadDecoder
import com.dashdevs.qr_nbu.decoder.QrPayloadDecoderImpl
import com.dashdevs.qr_nbu.manager.QrCodeManager
import com.dashdevs.qr_nbu.manager.QrCodeManagerBuilder
import com.dashdevs.qr_nbu.model.PaymentDataToEncode
import com.dashdevs.qr_nbu.model.QrResult
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal

private const val QR_FILE_NAME = "validNbuQr.png"

@RunWith(AndroidJUnit4::class)
class QRCodeDecodeEncodeTest {

    private val qrManager: QrCodeManager = QrCodeManagerBuilder().build()
    private val decoder: QrPayloadDecoder = QrPayloadDecoderImpl()

    @Test
    fun testDecoderWithEncoderIntegration() {
        val stream = javaClass.classLoader?.getResourceAsStream(QR_FILE_NAME)
        val nbuQR = BitmapFactory.decodeStream(stream)
        val qrDataFromNbu = decodeQR(nbuQR)
        val detailsNbu =
            (decoder.decodeQRData(qrDataFromNbu) as QrResult.DecodeResult.Success).details

        val data = PaymentDataToEncode(
            detailsNbu.beneficiary,
            detailsNbu.beneficiaryAccount,
            detailsNbu.amount ?: BigDecimal.ZERO,
            detailsNbu.beneficiaryCode,
            detailsNbu.paymentPurpose
        )

        val sdkQr = (qrManager.generateQr(data, nbuQR.width, nbuQR.height)
            as QrResult.GenerateQrResult.Success).qr

        val qrDataFromSdk = decodeQR(sdkQr)
        val detailsSdk =
            (decoder.decodeQRData(qrDataFromSdk) as QrResult.DecodeResult.Success).details
        assertEquals(detailsNbu, detailsSdk)
    }

    @Throws(Exception::class)
    private fun decodeQR(qrBitmap: Bitmap): String {
        val intArray = IntArray(qrBitmap.width * qrBitmap.height)
        qrBitmap.getPixels(intArray, 0, qrBitmap.width, 0, 0, qrBitmap.width, qrBitmap.height)
        val bitmap = BinaryBitmap(
            HybridBinarizer(
                RGBLuminanceSource(
                    qrBitmap.width,
                    qrBitmap.height,
                    intArray
                )
            )
        )
        return MultiFormatReader().decode(bitmap).text
    }
}