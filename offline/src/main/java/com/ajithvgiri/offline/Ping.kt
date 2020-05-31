package com.ajithvgiri.offline

import android.content.Context
import android.os.AsyncTask

class Ping : AsyncTask<Context, Void, Boolean>() {

    var internetConnectionCallback: InternetConnectionCallback? = null

    override fun doInBackground(vararg params: Context?): Boolean {
        params[0]?.let {
            return Utils.hasActiveInternetConnection()
        }
        return false
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        internetConnectionCallback?.hasActiveInternetConnection(result)
    }

}