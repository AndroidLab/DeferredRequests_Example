package com.alab.deferredrequests_example

import java.lang.RuntimeException

/**
 * Представляет карту с исполнителями для отложенных запросов.
 */
class DeferredExecutorsMap: IDeferredExecutorsMap {
    private val commandsMap = mutableMapOf<Class<out IDeferredRequestModel>, IDeferredRequestExecutor<IDeferredRequestModel>>()

    override fun getRequestExecutor(
        requestModel: IDeferredRequestModel
    ): IDeferredRequestExecutor<IDeferredRequestModel> =
        commandsMap[requestModel::class.java] ?: throw RuntimeException("Не найден исполнитель для класса модели")

    override fun addRequestExecutor(assuranceCommandMap: Map<Class<out IDeferredRequestModel>, IDeferredRequestExecutor<IDeferredRequestModel>>) {
        commandsMap.putAll(assuranceCommandMap)
    }
}