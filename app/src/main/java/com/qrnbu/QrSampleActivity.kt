package com.qrnbu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dashdevs.qr_nbu.manager.QrCodeManagerBuilder
import com.dashdevs.qr_nbu.model.PaymentDataToEncode
import com.dashdevs.qr_nbu.model.PaymentDetails
import com.dashdevs.qr_nbu.model.QrResult
import java.math.BigDecimal
import kotlinx.android.synthetic.main.activity_main.amount_et as amountEditText
import kotlinx.android.synthetic.main.activity_main.beneficiary_account_et as beneficiaryAccountEditText
import kotlinx.android.synthetic.main.activity_main.beneficiary_code_et as beneficiaryCodeEditText
import kotlinx.android.synthetic.main.activity_main.beneficiary_et as beneficiaryEditText
import kotlinx.android.synthetic.main.activity_main.error_tv as errorTextView
import kotlinx.android.synthetic.main.activity_main.generate_qr_btn as generateQRBtn
import kotlinx.android.synthetic.main.activity_main.payment_purpose_et as paymentPurposeEditTExt
import kotlinx.android.synthetic.main.activity_main.qr_iv as qrImage

class QrSampleActivity : AppCompatActivity() {

    private val qrManager by lazy { QrCodeManagerBuilder().build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generateQRBtn.setOnClickListener {
            generateQR()
        }
    }

    private fun generateQR() {
        hideError()

        val amountString = amountEditText.text.toString()
        val data = PaymentDataToEncode(
            beneficiaryEditText.text.toString(),
            beneficiaryAccountEditText.text.toString(),
            if (amountString.isEmpty()) BigDecimal.ZERO else amountString.toBigDecimal(),
            beneficiaryCodeEditText.text.toString(),
            paymentPurposeEditTExt.text.toString()
        )

        runCatching {
            qrManager.generateQr(data) {
                when (it) {
                    is QrResult.GenerateQrResult.Success -> qrImage.setImageBitmap(it.qr)
                    is QrResult.GenerateQrResult.Error -> setError(it.error.error)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        qrManager.handleScanResult(requestCode, resultCode, data)?.let { result ->
            val content = result.content
            if (content != null) {
                qrManager.decodeQrData(content) {
                    when (it) {
                        is QrResult.DecodeResult.Success -> {
                            parseDetails(it.details)
                            hideError()
                        }
                        is QrResult.DecodeResult.Error -> setError(it.error.error)
                    }
                }
            } else {
                Log.e("ScanResult", "QR content is null")
            }
        } ?: super.onActivityResult(requestCode, resultCode, data)
    }

    private fun hideError() {
        errorTextView.isVisible = false
    }

    private fun setError(error: String) {
        errorTextView.apply {
            text = error
            isVisible = true
        }
    }

    private fun parseDetails(details: PaymentDetails) =
        with(details) {
            beneficiaryEditText.setText(beneficiary)
            beneficiaryAccountEditText.setText(beneficiaryAccount)
            amountEditText.setText(amount?.toPlainString() ?: "")
            beneficiaryCodeEditText.setText(beneficiaryCode)
            paymentPurposeEditTExt.setText(paymentPurpose)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_scan -> {
                qrManager.startScan(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
