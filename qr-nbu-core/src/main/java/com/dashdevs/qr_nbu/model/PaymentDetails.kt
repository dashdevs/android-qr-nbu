package com.dashdevs.qr_nbu.model

import java.math.BigDecimal

/**
 * A class that encapsulates all necessary QR data elements according to
 * National Bank of Ukraine QR structure standard
 *
 * @property tag A mandatory service tag, must always be “BCD”
 * @property version A current version of QR data format, e.g. "001"
 * @property encoding A current value of encoding, e.g. "1" involves the use of UTF-8 encoding
 *           for all fields that may contain Cyrillic characters
 * @property function A function element, e.g. for the format version "001" only the value “UCT”
 *           is allowed ("Ukrainian Credit Transfer")
 * @property bic An element BIC, reserved field for QR data version "001", must be empty for version "001"
 * @property beneficiary A beneficiary info, e.g. "OOO «Водопостачання»
 * Contains last name, first name, middle name of the physical person or name of legal entity
 * @property beneficiaryAccount A beneficiary account number, e.g. "UA783226690000026005012107132"
 * @property currency A payment currency code, must be "UAH" for version "001"
 * @property amount An amount to transfer (value range: 0 - 999999999.99)
 * @property beneficiaryCode An beneficiary code, e.g. "40723825"
 *           May contain one of three options:
 *            - identification code of beneficiary - natural person (РНОКПП отримувача – фізичної особи)
 *            - series (if available) and passport number of the beneficiary
 *              (серію (за наявності) та номер паспорта отримувача – фізичної особи)
 *            - unique code of the legal entity
 *              (реєстраційний (обліковий) номер платника податку отримувача - юридичної особи)
 * @property goal An element Goal, reserved field for QR data version "001", must be empty for
 *           version "001"
 * @property reference An element Reference, reserved field for QR data version "001", must be
 *           empty for version "001"
 * @property paymentPurpose A payment information.
 * @property display An element Display, reserved field for QR data version "001", must be
 *           empty for version "001"
 */
data class PaymentDetails(
    val tag: String = MANDATORY_SERVICE_TAG,
    val version: String = CURRENT_VERSION,
    val encoding: String = ENCODING_VALUE,
    val function: String = FUNCTION_VALUE,
    val bic: String = EMPTY_VALUE,
    val beneficiary: String,
    val beneficiaryAccount: String,
    val currency: String = UAH_CURRENCY_CODE,
    val amount: BigDecimal?,
    val beneficiaryCode: String,
    val goal: String = EMPTY_VALUE,
    val reference: String = EMPTY_VALUE,
    val paymentPurpose: String,
    val display: String = EMPTY_VALUE
) {
    /**
     * This method is used to join all Payment details elements in a proper sequence with
     * [ELEMENT_SEPARATOR] to [String] object
     *
     * @return The QR data[String] in a proper form, according to
     * National Bank of Ukraine QR structure standard
     */
    fun getQrData() =
        arrayOf(
            tag,
            version,
            encoding,
            function,
            bic,
            beneficiary,
            beneficiaryAccount,
            "$currency${amount?.toPlainString() ?: ""}",
            beneficiaryCode,
            goal,
            reference,
            paymentPurpose,
            display
        ).joinToString(separator = ELEMENT_SEPARATOR, postfix = ELEMENT_SEPARATOR)

    companion object {
        /**
         * A constant that defines a mandatory service tag value
         *
         * @see[tag]
         */
        const val MANDATORY_SERVICE_TAG = "BCD"

        /**
         * A constant that defines a current encoding value
         *
         * @see[encoding]
         */
        const val ENCODING_VALUE = "1"

        /**
         * A constant that defines a current function value
         *
         * @see[function]
         */
        const val FUNCTION_VALUE = "UCT"

        /**
         * A constant that defines a current currency code value
         *
         * @see[currency]
         */
        const val UAH_CURRENCY_CODE = "UAH"

        /**
         * A constant that defines a current version of QR data structure
         *
         * @see[version]
         */
        private const val CURRENT_VERSION = "001"

        /**
         * An empty [String] value
         */
        private const val EMPTY_VALUE = ""

        /**
         * A constant that defines a separator between payment details elements
         */
        private const val ELEMENT_SEPARATOR = "\n"
    }
}