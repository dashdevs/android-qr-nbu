package com.dashdevs.qr_nbu.model

enum class QrDataError(val error: String) {
    ERROR_EMPTY_DATA("Data can't be null or empty"),
    ERROR_INVALID_INPUT_SIZE("Data size can't be more than 331 byte"),
    ERROR_INVALID_MANDATORY_SERVICE_TAG("Data payload must start with mandatory service tag - BCD"),
    ERROR_INVALID_ELEMENTS_SIZE("Data payload must contains 13 elements"),
    ERROR_NON_SUPPORTED_QR_VERSION("Current QR version isn't supported"),
    ERROR_INVALID_ENCODING_VALUE("Encoding value isn't valid"),
    ERROR_INVALID_FUNCTION_VALUE("Function value isn't valid"),
    ERROR_INVALID_BIC_VALUE("Bank Identifier Code value isn't valid"),
    ERROR_INVALID_GOAL_VALUE("Goal value isn't valid"),
    ERROR_INVALID_REFERENCE_VALUE("Reference value isn't valid"),
    ERROR_INVALID_DISPLAY_VALUE("Display value isn't valid"),
    ERROR_INVALID_CURRENCY_VALUE("Currency value isn't valid"),
    ERROR_INVALID_AMOUNT("Amount value isn't valid, should be positive and no more than 999999999.99"),
    ERROR_INVALID_BENEFICIARY_VALUE("Beneficiary value must not be empty"),
    ERROR_INVALID_BENEFICIARY_ACCOUNT_VALUE("Beneficiary Account value must not be empty"),
    ERROR_INVALID_BENEFICIARY_CODE_VALUE("Beneficiary Code value must not be empty"),
    ERROR_INVALID_PAYMENT_PURPOSE_VALUE("Payment Purpose value must not be empty")
}