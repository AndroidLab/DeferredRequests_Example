package com.alab.deferredrequests_example

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Описывает методы запросов к Vkontakte.
 */
interface IVKApiService {

    /**
     * Возвращает список записей со стены пользователя или сообщества по указанному id.
     */
    @GET("wall.get")
    suspend fun getWall(
        @Query("owner_id") owner_id: String
    ): Response<ResponseBody>
}