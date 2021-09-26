package com.alab.deferredrequests_example

/**
 * Описывает методы для карты с отложенными запросами.
 */
interface IDeferredExecutorsMap {
    /**
     * Возвращает класс исполнителя.
     * @param deferredRequestDataClass Класс модели с данными для запроса.
     */
    fun getRequestExecutor(
        deferredRequestDataClass: IDeferredRequestModel
    ): IDeferredRequestExecutor<IDeferredRequestModel>

    /**
     * Добавляет карту для гарантированного запроса.
     * @param assuranceCommandMap Карта для гарантированного запроса.
     */
    fun addRequestExecutor(assuranceCommandMap: Map<Class<out IDeferredRequestModel>, IDeferredRequestExecutor<IDeferredRequestModel>>)
}