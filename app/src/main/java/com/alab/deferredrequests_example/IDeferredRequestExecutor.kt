package com.alab.deferredrequests_example

import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Описывает метод исполнителя на отправку запроса.
 */
interface IDeferredRequestExecutor<T> {
    /**
     * Выполняет запрос.
     * @param deferredRequestModel Данные для запроса.
     */
    suspend fun execute(deferredRequestModel: T): Response<ResponseBody>
}