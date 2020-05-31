package com.ajithvgiri.offline

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


object Utils {

    /**
     * Check if the device is connected with the Internet.
     */
    @JvmStatic
    fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    /**
     * Check if the device is in airplane mode.
     */
    @JvmStatic
    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    /**
     * Ping google.com to check if the internet connection is active.
     * It must be called from a background thread.
     */
    @JvmStatic
    fun hasActiveInternetConnection(): Boolean {
        return try {
            val urlConnection = URL("https://www.google.com").openConnection() as HttpURLConnection

            urlConnection.setRequestProperty("User-Agent", "Test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1500
            urlConnection.connect()

            urlConnection.responseCode == 200
        } catch (e: IOException) {
            Log.e("Offline Utils", e.localizedMessage)
            e.printStackTrace()
            false
        }
    }

    /**
     * Open the system settings.
     */
    @JvmStatic
    fun turnOnMobileData(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Open the wifi settings.
     */
    @JvmStatic
    fun turnOnWifi(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Open the airplane mode settings.
     */
    @JvmStatic
    fun turnOffAirplaneMode(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Checking internet connection is available
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP -> {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }
            else -> {
                return false
            }
        }
        return return if (result) {
            hasActiveInternetConnection()
        } else {
            result
        }
    }
}