package com.dashdevs.qr_nbu

import com.dashdevs.qr_nbu.model.PaymentDetails

internal const val ELEMENT_SEPARATOR = "\n"

internal object FieldKey {
    const val SERVICE_TAG = "SERVICE_TAG"
    const val VERSION = "VERSION"
    const val ENCODING = "ENCODING"
    const val FUNCTION = "FUNCTION"
    const val BIC = "BIC"
    const val BENEFICIARY = "BENEFICIARY"
    const val BENEFICIARY_ACCOUNT = "BENEFICIARY_ACCOUNT"
    const val CURRENCY_AMOUNT = "CURRENCY_AMOUNT"
    const val BENEFICIARY_CODE = "BENEFICIARY_CODE"
    const val GOAL = "GOAL"
    const val REFERENCE = "REFERENCE"
    const val PAYMENT_PURPOSE = "PAYMENT_PURPOSE"
    const val DISPLAY = "DISPLAY"
}

internal object ValidField {
    const val SERVICE_TAG = "BCD"
    const val VERSION = "001"
    const val ENCODING = "1"
    const val FUNCTION = "UCT"
    const val BIC = ""
    const val BENEFICIARY = "ПрАТ АК «Водопостачання»"
    const val BENEFICIARY_ACCOUNT = "UA783226690000026005012107132"
    const val CURRENCY_AMOUNT = "UAH576.56"
    const val AMOUNT = "576.56"
    const val BENEFICIARY_CODE = "40723825"
    const val GOAL = ""
    const val REFERENCE = ""
    const val PAYMENT_PURPOSE = "Сплата за червень 2019"
    const val DISPLAY = ""
}

internal val validPaymentDetails = PaymentDetails(
    beneficiary = ValidField.BENEFICIARY,
    beneficiaryAccount = ValidField.BENEFICIARY_ACCOUNT,
    amount = ValidField.AMOUNT.toBigDecimal(),
    beneficiaryCode = ValidField.BENEFICIARY_CODE,
    paymentPurpose = ValidField.PAYMENT_PURPOSE
)

internal val validPaymentDetailsMap: LinkedHashMap<String, String>
    get() = linkedMapOf(
        FieldKey.SERVICE_TAG to ValidField.SERVICE_TAG,
        FieldKey.VERSION to ValidField.VERSION,
        FieldKey.ENCODING to ValidField.ENCODING,
        FieldKey.FUNCTION to ValidField.FUNCTION,
        FieldKey.BIC to ValidField.BIC,
        FieldKey.BENEFICIARY to ValidField.BENEFICIARY,
        FieldKey.BENEFICIARY_ACCOUNT to ValidField.BENEFICIARY_ACCOUNT,
        FieldKey.CURRENCY_AMOUNT to ValidField.CURRENCY_AMOUNT,
        FieldKey.BENEFICIARY_CODE to ValidField.BENEFICIARY_CODE,
        FieldKey.GOAL to ValidField.GOAL,
        FieldKey.REFERENCE to ValidField.REFERENCE,
        FieldKey.PAYMENT_PURPOSE to ValidField.PAYMENT_PURPOSE,
        FieldKey.DISPLAY to ValidField.DISPLAY
    )

internal val validData = validPaymentDetailsMap.joinValuesWithSeparator()

internal fun HashMap<String, String>.joinValuesWithSeparator() =
    values.joinToString(
        separator = ELEMENT_SEPARATOR,
        postfix = ELEMENT_SEPARATOR
    )