package com.ajithvgiri.offlinedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ajithvgiri.offline.NoInternetConnectionBuilder
import com.ajithvgiri.offline.NoInternetConnectionSnackBar

class MainActivity : AppCompatActivity() {

    var noInternetConnectionSnackBar: NoInternetConnectionSnackBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        noInternetConnectionSnackBar = NoInternetConnectionBuilder(applicationContext, findViewById(android.R.id.content)).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        noInternetConnectionSnackBar?.destroy()
    }
}