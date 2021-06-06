package com.sunnyweather.android.logic.model

// 封装
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)