package com.alab.deferredrequests_example

import java.util.*

/**
 * Описывает модель отложенного запроса.
 */
interface IDeferredRequestModel {
    /**
     * Возвращает заголовок запроса.
     */
    val title: String

    /**
     * Возвращает описание запроса.
     */
    val description: String

    /**
     * Возвращает идентификатор пользователя.
     */
    var userId: UUID?

    /**
     * Возвращает имя пользователя.
     */
    var userName: String?

    /**
     * Возвращает id запроса.
     */
    var requestId: UUID?

    /**
     * Возвращает время запроса.
     */
    var date: String?
}