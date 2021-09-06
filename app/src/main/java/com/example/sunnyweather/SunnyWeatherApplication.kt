package com.example.sunnyweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {

  companion object{
    lateinit var context: Context
    const val TOKEN = "QUAJQ2hpT11IUqfs"
  }

  override fun onCreate() {
    super.onCreate()
    context = applicationContext
  }
}
