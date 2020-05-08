package com.dashdevs.qr_nbu.manager

/**
 * Builder class to be used for set up [QrCodeManager] and provide its instance
 */
class QrCodeManagerBuilder {
    /**
     * This method combines all of the options that have been set and return a new [QrCodeManager]
     * object.
     */
    fun build() = QrCodeManagerImpl.newInstance()
}