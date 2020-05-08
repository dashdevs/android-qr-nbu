package com.dashdevs.qr_nbu.encoder

import com.dashdevs.qr_nbu.model.QrResult
import java.math.BigDecimal

/**
 * Interface to be extended by [QrPayloadEncoderImpl]
 *
 * This interface is used for encoding mandatory payment data to Qr code data
 * according to National Bank of Ukraine QR structure standard
 */
internal interface QrPayloadEncoder {
    /**
     * This method is used to encode mandatory payment data to [String] data object,
     * or return an error [com.dashdevs.qr_nbu.model.QrDataError] and wrap the return value to [QrResult.EncodeResult]
     *
     * @param beneficiary A beneficiary info, e.g. "OOO «Водопостачання»
     * Contains last name, first name, middle name of the physical person or name of legal entity
     * @param beneficiaryAccount A beneficiary account number, e.g. "UA783226690000026005012107132"
     * @param amount An amount to transfer (value range: 0 - 999999999.99)
     * @param beneficiaryCode An beneficiary code, e.g. "40723825"
     * May contain one of three options:
     *  - identification code of beneficiary - natural person (РНОКПП отримувача – фізичної особи)
     *  - series (if available) and passport number of the beneficiary
     *    (серію (за наявності) та номер паспорта отримувача – фізичної особи)
     *  - unique code of the legal entity
     *    (реєстраційний (обліковий) номер платника податку отримувача - юридичної особи)
     * @param paymentPurpose A payment information.
     * @return The result of encoding [QrResult.EncodeResult]
     */
    fun encodeQRData(
        beneficiary: String,
        beneficiaryAccount: String,
        amount: BigDecimal,
        beneficiaryCode: String,
        paymentPurpose: String
    ): QrResult.EncodeResult
}