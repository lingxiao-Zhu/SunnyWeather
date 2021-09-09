package com.example.sunnyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException

object Repository {

  fun searchPlaces(query: String) = fire(Dispatchers.IO) {
    val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
    if (placeResponse.status == "ok") {
      val places = placeResponse.places
      Result.success(places)
    } else {
      Result.failure(RuntimeException("response status is $placeResponse.status"))
    }
  }

  fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
    Log.d("refreshWeather", "lng: $lng, lat: $lat")
    coroutineScope {
      val deferredRealtime = async {
        SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
      }
      val deferredDaily = async {
        SunnyWeatherNetwork.getDailyWeather(lng,lat)
      }
      val realtimeRes = deferredRealtime.await()
      val dailyRes = deferredDaily.await()
      if(realtimeRes.status == "ok" && dailyRes.status == "ok"){
        val weather = Weather(realtimeRes.result.realtime, dailyRes.result.daily)
        Result.success(weather)
      }else{
        Result.failure(
          RuntimeException(
            "realtime response status is ${realtimeRes.status}" +
              "daily response status is ${dailyRes.status}"
          ))
      }
    }
  }

  fun <T> fire(context: CoroutineDispatcher, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
    val result = try {
        block()
    }catch (e: java.lang.Exception){
      Result.failure<T>(e)
    }
    emit(result)
  }

}
