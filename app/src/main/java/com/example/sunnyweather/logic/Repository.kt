package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.lang.Exception
import java.lang.RuntimeException

object Repository {
  fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
    val result = try {
        val placeRes = SunnyWeatherNetwork.searchPlaces(query)
        if(placeRes.status == "ok"){
          val places = placeRes.places
          Result.success(places)
        }else{
          Result.failure(RuntimeException("res status is ${placeRes.status}"))
        }
    } catch (e: Exception){
      Result.failure<List<Place>>(e)
    }
    emit(result as Dispatchers)
  }
}
