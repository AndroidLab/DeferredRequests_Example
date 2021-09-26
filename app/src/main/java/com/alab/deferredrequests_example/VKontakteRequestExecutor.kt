package com.alab.deferredrequests_example

import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Представляет исполнителя для запроса к ВКонтакте.
 * @param vkApiService Сервис запросов к ВКонтакте.
 */
class VKontakteRequestExecutor (
    private val vkApiService: IVKApiService
) : IDeferredRequestExecutor<VKontakteRequestModel> {
    override suspend fun execute(deferredRequestModel: VKontakteRequestModel): Response<ResponseBody> {
        return vkApiService.getWall(deferredRequestModel.ownerId)
    }
}