package com.alab.deferredrequests_example

/**
 * Перечисляет состояния выполнения команд.
 */
enum class ExecuteStates {
    /**
     * Нету действий.
     */
    NONE,
    /**
     * Выполняет.
     */
    INPROGRESS,
    /**
     * Успех.
     */
    SUCCESS
}