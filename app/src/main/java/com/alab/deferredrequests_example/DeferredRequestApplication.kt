package com.alab.deferredrequests_example

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Представляет приложение.
 */
class DeferredRequestApplication : Application() {

    var retrofit = Retrofit.Builder().baseUrl("https://api.vk.com/method/").addConverterFactory(GsonConverterFactory.create()).build()  // Retrofit.
    lateinit var deferredRequestsService: IDeferredRequestsService   // Сервис выполнения отложженых запросов к серверу.
    lateinit var deferredRequestStorage: IDeferredRequestStorage   // Хранилище для отложенных запросов.
    lateinit var deferredExecutorsMap: IDeferredExecutorsMap   // Хранилище для отложенных запросов.

    companion object {
        lateinit var application: DeferredRequestApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        deferredRequestStorage = DeferredRequestStorage()
        deferredExecutorsMap = DeferredExecutorsMap()
        deferredRequestsService = DeferredRequestsService(this, deferredRequestStorage, deferredExecutorsMap)
    }

}