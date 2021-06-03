package com.sunnyweather.android

import android.app.Application
import android.content.Context

/**
 * 全局获取Context实例
 * 全局获取Token
 */
class SunnyWeatherApplication : Application() {

    companion object {
        const val TOKEN = "hu1VWh6FZqjVrcb2"  // https://api.caiyunapp.com/v2/
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}