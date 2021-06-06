package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

// 未来几天天气信息api返回的数据模型类
data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(val temperature: Float, val skycon: String, @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: Aqi)

    data class Aqi(val chn: Float)
}


