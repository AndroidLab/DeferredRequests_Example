package com.alab.deferredrequests_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Представляет главный экран приложения.
 */
class MainActivity : AppCompatActivity() {

    private val deferredRequestsService = DeferredRequestApplication.application.deferredRequestsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testRequest = DeferredRequestApplication.application.retrofit.create(IVKApiService::class.java)
        //Создаем карту, где ключ это класс нашей моли запроса, а значение это исполнитель для этого запроса
        deferredRequestsService.registerDeferredExecutorMap(mapOf(VKontakteRequestModel::class.java to VKontakteRequestExecutor(testRequest) as IDeferredRequestExecutor<IDeferredRequestModel>))

        findViewById<Button>(R.id.deferredRequestsBtn).setOnClickListener {
            startActivity(Intent(this, DeferredRequestsActivity::class.java))
        }

        findViewById<Button>(R.id.sendRequestBtn).setOnClickListener {
            lifecycleScope.launch {
                //Создаем модель нашего запроса
                val vkontakteRequestModel = VKontakteRequestModel(
                    ownerId = "-1"   //-1 это id главной группы вк, https://vk.com/club1
                )
                //Пытаемся выполнить запрос через наш сервис, если интернет есть, запрос успешно выполнится, если нет, будет отложен к кэш
                deferredRequestsService.handleRequest(vkontakteRequestModel)
            }
        }
    }
}