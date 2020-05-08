package com.dashdevs.qr_nbu.util

/**
 * A constant that defines the maximal number of bytes in QR data payload
 */
const val MAX_DATA_SIZE = 331

/**
 * This method is used to determine whether the size of Qr data payload is correct or not
 *
 * @return true if size is not valid, false otherwise
 */
fun String.sizeIsNotValid() = toByteArray().size > MAX_DATA_SIZE