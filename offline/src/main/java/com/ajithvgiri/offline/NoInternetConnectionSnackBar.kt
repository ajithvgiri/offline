package com.ajithvgiri.offline

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.view.ViewGroup
import com.ajithvgiri.offline.Utils.isAirplaneModeOn
import com.ajithvgiri.offline.Utils.isConnectedToInternet
import com.ajithvgiri.offline.Utils.turnOffAirplaneMode
import com.ajithvgiri.offline.Utils.turnOnMobileData
import com.google.android.material.snackbar.Snackbar

class NoInternetConnectionSnackBar constructor(
    private val context: Context,
    private val parent: ViewGroup,
    private val indefinite: Boolean,
    private val noInternetConnectionMessage: String,
    private val onAirplaneModeMessage: String,
    private val snackBarActionText: String,
    private val showActionToDismiss: Boolean,
    private val snackBarDismissActionText: String
) {

    private lateinit var snackBar: Snackbar

    private var isDismissed = true

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    var internetConnectionCallback: InternetConnectionCallback? = null

    init {
        initViews()
        initReceivers()
    }

    private fun initViews() {
        snackBar = Snackbar.make(
            parent,
            noInternetConnectionMessage,
            if (indefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        )
    }

    private fun updateViews() {
        if (isAirplaneModeOn(context)) {
            snackBar.setText(onAirplaneModeMessage)
            updateActionButton(true)
        } else {
            snackBar.setText(noInternetConnectionMessage)
            updateActionButton(false)
        }
    }

    private fun updateActionButton(isAirplaneModeOn: Boolean) {
        if (showActionToDismiss) {
            snackBar.setAction(
                snackBarDismissActionText
            ) {
                // dismiss
            }

        } else {
            snackBar.setAction(
                snackBarActionText
            ) {
                if (isAirplaneModeOn) {
                    turnOffAirplaneMode(context)
                } else {
                    turnOnMobileData(context)
                }
            }

        }
    }

    private fun initReceivers() {

        updateConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(
                    getConnectivityManagerCallback()
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val builder = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                connectivityManager.registerNetworkCallback(
                    builder.build(),
                    getConnectivityManagerCallback()
                )
            }
        }

    }

    private fun updateConnection() {
        val result = isConnectedToInternet(context)
        if (result) {
            snackBar.dismiss()
        } else {
            show()
        }
        internetConnectionCallback?.hasActiveInternetConnection(result)
    }


    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network?) {
                    dismiss()
                    internetConnectionCallback?.hasActiveInternetConnection(true)
                }

                override fun onLost(network: Network?) {
                    show()
                    internetConnectionCallback?.hasActiveInternetConnection(false)
                }
            }

            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("This should not happened!")
        }
    }

    fun dismiss() {
        if (!isDismissed) {
            isDismissed = true
            snackBar.dismiss()
        }
    }

    fun show() {
        Ping().apply {
            internetConnectionCallback = object :
                InternetConnectionCallback {
                override fun hasActiveInternetConnection(hasActiveInternetConnection: Boolean) {
                    if (!hasActiveInternetConnection) {
                        isDismissed = false
                        updateViews()
                        snackBar.show()
                    }
                }
            }
            execute(context)
        }
    }

    fun destroy() {
        snackBar.dismiss()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        }
    }
}