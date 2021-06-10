package com.sunnyweather.android.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("** Base Activity **", "On Create ${javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("** Base Activity **", "On Destroy ${javaClass.simpleName}")
    }
}