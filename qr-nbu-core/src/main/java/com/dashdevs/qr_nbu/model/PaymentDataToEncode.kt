package com.dashdevs.qr_nbu.model

import java.math.BigDecimal

/**
 * A class that encapsulates mandatory payment data
 *
 * @property beneficiary A beneficiary info, e.g. "OOO «Водопостачання»
 * Contains last name, first name, middle name of the physical person or name of legal entity
 * @property beneficiaryAccount A beneficiary account number, e.g. "UA783226690000026005012107132"
 * @property amount An amount to transfer (value range: 0 - 999999999.99)
 * @property beneficiaryCode An beneficiary code, e.g. "40723825"
 * May contain one of three options:
 *  - identification code of beneficiary - natural person (РНОКПП отримувача – фізичної особи)
 *  - series (if available) and passport number of the beneficiary
 *    (серію (за наявності) та номер паспорта отримувача – фізичної особи)
 *  - unique code of the legal entity
 *    (реєстраційний (обліковий) номер платника податку отримувача - юридичної особи)
 * @property paymentPurpose A payment information.
 */
data class PaymentDataToEncode(
    val beneficiary: String,
    val beneficiaryAccount: String,
    val amount: BigDecimal,
    val beneficiaryCode: String,
    val paymentPurpose: String
)