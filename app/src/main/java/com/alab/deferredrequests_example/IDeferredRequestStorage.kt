package com.alab.deferredrequests_example

import kotlinx.coroutines.flow.SharedFlow

/**
 * Описывает методы кэширования запросов.
 */
interface IDeferredRequestStorage {
    /**
     * Возвращает список не отправленных запросов.
     */
    val requestsFlow: SharedFlow<List<IDeferredRequestModel>>

    /**
     * Возвращает количество элементов в кэше.
     */
    val countElements: Int

    /**
     * Возвращает модель запроса из кэша.
     * @param position Позиция запроса в кэше.
     */
    fun getRequestModel(position: Int): IDeferredRequestModel?

    /**
     * Добавляет модель запроса в кэш.
     * @param deferredRequestModel Данные запроса.
     * @return Возвращает результат добавления запроса в кэш.
     */
    fun addRequestModel(deferredRequestModel: IDeferredRequestModel): Boolean

    /**
     * Удаляет модель запроса из кэша.
     * @param deferredRequestModel Данные запроса.
     */
    fun removeRequestModel(deferredRequestModel: IDeferredRequestModel)

    /**
     * Удаляет все модели запросов из кэша.
     */
    fun removeAllRequestsModel()
}