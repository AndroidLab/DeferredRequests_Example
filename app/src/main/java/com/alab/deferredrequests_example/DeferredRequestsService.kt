package com.alab.deferredrequests_example

import android.content.Context
import android.widget.Toast
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Представляет сервис выполнения отложженых запросов к серверу.
 * @param context Контекст приложения.
 * @param deferredRequestStorage Кэш для хранения отложенных запросов.
 * @param deferredRequestsMap Карта с отложенными запросами.
 */
class DeferredRequestsService(
    private val context: Context,
    private val deferredRequestStorage: IDeferredRequestStorage,
    private val deferredRequestsMap: IDeferredExecutorsMap
) : IDeferredRequestsService {
    private val _repeatJob = CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            repeatRequest()
            delay(5000)
        }
    }

    private suspend fun repeatRequest(): Boolean {
        if (deferredRequestStorage.countElements > 0) {
            val requestModel = deferredRequestStorage.getRequestModel(0)
            if (requestModel != null && handleRequest(requestModel)) {
                deferredRequestStorage.removeRequestModel(requestModel)
                repeatRequest()
            } else {
                return false
            }
        } else {
            return true
        }
        return true
    }

    override suspend fun handleRequest(
        requestModel: IDeferredRequestModel
    ): Boolean {
        //Если не был указан id пользователя
        if (requestModel.userId == null) {
            requestModel.userId = UUID.fromString("12eb6997-6c60-4828-84e1-d4e211ba00a6")
        }
        //Если не было указано имя пользователя
        if (requestModel.userName == null) {
            requestModel.userName = "Иванов Иван Иванович"
        }
        //Если не был указан id запроса
        if (requestModel.requestId == null) {
            requestModel.requestId = UUID.randomUUID()
        }
        //Если не было указано время запроса
        if (requestModel.date == null) {
            requestModel.date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
        }

        var isSuccess = true
        try {
            val response = deferredRequestsMap.getRequestExecutor(requestModel).execute(requestModel)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, response.raw().toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            when (e) {
                //По эти ошибкам определяем, что запрос не был выполнен
                is SocketTimeoutException, is ConnectException, is UnknownHostException -> {
                    isSuccess = false
                }
                else -> throw e
            }
        }

        return if (isSuccess) {
            true
        } else {
            if (deferredRequestStorage.addRequestModel(requestModel)) {
                Toast.makeText(context, "Запрос был добавлен в кэш", Toast.LENGTH_LONG).show()
            }
            false
        }
    }

    override suspend fun retryHandleRequests():Boolean {
        stopService()
        val success = repeatRequest()
        startService()
        return success
    }

    override fun startService() {
        _repeatJob.start()
    }

    override fun stopService() {
        _repeatJob.cancel()
    }

    override fun registerDeferredExecutorMap(deferredRequestExecutorMap: Map<Class<out IDeferredRequestModel>, IDeferredRequestExecutor<IDeferredRequestModel>>) {
        deferredRequestsMap.addRequestExecutor(deferredRequestExecutorMap)
    }
}