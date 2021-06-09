package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    // 在PlaceDao的基础上再做一次封装
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {   // 因为要用async所以创建一个协程作用域
            // 使用async异步并发调用
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }

            // 使用await确保realtime和dailytime确保都获取完成
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()

            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                // 进一步封装，使得一次调用refreshWeather方法既可以得到realtime又可以得到dailytime
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                        RuntimeException(
                                "realtime response status is ${realtimeResponse.status}" +
                                        "daily response status is ${dailyResponse.status}"
                        )
                )
            }
        }
    }

    // 高阶函数
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
            liveData<Result<T>>(context) {
                val result = try {
                    block()
                } catch (e: Exception) {
                    Result.failure<T>(e)
                }
                emit(result)
            }
}