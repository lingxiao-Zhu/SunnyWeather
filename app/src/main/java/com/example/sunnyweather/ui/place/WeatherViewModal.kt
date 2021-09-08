package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {

  private val locationLiveData = MutableLiveData<Location>()

  // 界面相关的数据，放到ViewModel中可以保证它们在手机屏幕发生旋转的时候不会丢失，稍后在编写UI层代码的时候会用到这几个变量。
  var locationLng = ""

  var locationLat = ""

  var placeName = ""

  val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
    Repository.refreshWeather(location.lng, location.lat)
  }

  fun refreshWeather(lng: String, lat: String) {
    locationLiveData.value = Location(lng, lat)
  }

}
