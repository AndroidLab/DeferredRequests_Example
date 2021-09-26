package com.alab.deferredrequests_example

/**
 * Описывает методы выполнения отложенных запросов к серверу.
 */
interface IDeferredRequestsService {
    /**
     * Выполняет запрос на сервер.
     * @param deferredRequestModel Данные для запроса к серверу.
     */
    suspend fun handleRequest(
        deferredRequestModel: IDeferredRequestModel
    ): Boolean

    /**
     * Пытается выполнить все запросы.
     */
    suspend fun retryHandleRequests(): Boolean

    /**
     * Запускает сервис.
     */
    fun startService()

    /**
     * Останавливает сервис.
     */
    fun stopService()

    /*
     * Регистрирует карту для отложенного запроса.
     */
    fun registerDeferredExecutorMap(deferredRequestExecutorMap: Map<Class<out IDeferredRequestModel>, IDeferredRequestExecutor<IDeferredRequestModel>>)
}