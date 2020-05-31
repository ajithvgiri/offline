package com.ajithvgiri.offline

import android.content.Context
import android.view.ViewGroup

class NoInternetConnectionBuilder(private val context: Context, private val parent: ViewGroup) {

    var indefinite = true
    var internetConnectionCallback: InternetConnectionCallback? = null
    var noInternetConnectionMessage = context.getString(R.string.default_snackbar_message)
    var onAirplaneModeMessage = context.getString(R.string.default_airplane_mode_snackbar_message)
    var snackBarActionText = context.getString(R.string.settings)
    var showActionToDismiss = false
    var snackBarDismissActionText = context.getString(R.string.ok)

    fun build(): NoInternetConnectionSnackBar {
        val noInternetSnackBar =
            NoInternetConnectionSnackBar(
                context,
                parent,
                indefinite,
                noInternetConnectionMessage,
                onAirplaneModeMessage,
                snackBarActionText,
                showActionToDismiss,
                snackBarDismissActionText
            )
        noInternetSnackBar.internetConnectionCallback = internetConnectionCallback
        return noInternetSnackBar
    }

}