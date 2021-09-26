package com.alab.deferredrequests_example

import java.util.*

/**
 * Представляет данные для запроса к ВКонтакте.
 * @param ownerId Возвращает id группы для запроса.
 */
data class VKontakteRequestModel(
    val ownerId: String,
    override val title: String = "Запрос к ВКонтакте",
    override val description: String = "Запрос на получение записей со стены группы ВК",
    override var userId: UUID? = null,
    override var userName: String? = null,
    override var requestId: UUID? = null,
    override var date: String? = null
) : IDeferredRequestModel