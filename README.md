# android-qr-nbu
An open source library for encoding, decoding and generating QR code according National Bank of Ukraine decree https://bank.gov.ua/admin_uploads/article/proekt_2020-04-06.pdf

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)	[![Version](https://img.shields.io/static/v1?label=Download&message=0.1.0&color=blue&style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

## Authors
[DashDevs LLC](https://www.dashdevs.com)

## Usage

Add the following dependency to your project `build.gradle` file:

`implementation 'com.dashdevs.qr-nbu:qr-nbu-core:$latest_version'`

If you want to use Camera for scanning QR code from sdk box:
```kotlin
// declare a QrCodeManager variable
private val qrManager:  QrCodeManager by lazy { QrCodeManagerBuilder().build() }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

	// start scanning process
    scanBtn.setOnClickListener {
           qrManager.startScan(this)
    }
}

// handle scanning result
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    qrManager.handleScanResult(requestCode, resultCode, data)?.let { result ->
        val content = result.content
        if (content != null) {
            qrManager.decodeQrData(content) {
                when (it) {
                    is QrResult.DecodeResult.Success -> // handle success result here
                    is QrResult.DecodeResult.Error -> // handle error here
                }
            }
        } else {
            Log.e("ScanResult", "QR content is null")
        }
    } ?: super.onActivityResult(requestCode, resultCode, data)
}
```
If you are using your own Qr Scanner you can use just `QrCodeManager` instance for decoding and encoding QR data:

```kotlin
// encoding mandatory payment parameters to QR string data
private fun encodeQR(
    beneficiary: String,
    beneficiaryAccount: String,
    amount: BigDecimal,
    beneficiaryCode: String,
    paymentPurpose: String
) {

    val data = PaymentDataToEncode(
        beneficiary,
        beneficiaryAccount,
        amount,
        beneficiaryCode,
        paymentPurpose
    )

    qrManager.encodeQrData(data) {
        when (it) {
            is QrResult.EncodeResult.Success -> // handle encoding success
            is QrResult.EncodeResult.Error -> // handle encoding error
        }
    }
}

// decoding Qr string data to PaymantDetails object
private fun decodeQr(data: String) {

    qrManager.decodeQrData(data) {
        when (it) {
            is QrResult.DecodeResult.Success -> handle decodinng success
            is QrResult.DecodeResult.Error -> handle decodinng error
        }
    }
}
```
## License
This sdk is licensed under the [Apache License](https://www.apache.org/licenses/LICENSE-2.0). Check [LICENSE](https://github.com/dashdevs/android-qr-nbu/blob/master/LICENSE) file for more information.