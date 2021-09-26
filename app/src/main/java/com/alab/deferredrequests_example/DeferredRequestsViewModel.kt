package com.alab.deferredrequests_example

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Определяет модель представления экрана с информацией по отложенным запросам.
 */
class DeferredRequestsViewModel : ViewModel() {
    private val deferredRequestsService = DeferredRequestApplication.application.deferredRequestsService   // Сервис выполнения отложженых запросов к серверу.
    private val deferredRequestStorage = DeferredRequestApplication.application.deferredRequestStorage   // Хранилище для отложенных запросов.

    private val _requests: MutableStateFlow<List<IDeferredRequestModel>> = MutableStateFlow(listOf())
    private val _executeState: MutableStateFlow<ExecuteStates> = MutableStateFlow(ExecuteStates.NONE)

    /**
     * Возвращает список запросов для выполнения.
     */
    val requests: StateFlow<List<IDeferredRequestModel>> = _requests.asStateFlow()

    /**
     * Возвращает состояние выполнения запросов.
     */
    val executeState: StateFlow<ExecuteStates> = _executeState.asStateFlow()

    /**
     * Возвращает флаг отвечающий за показ диалогового окна.
     */
    val isShowAlertDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            deferredRequestStorage.requestsFlow.collect {
                _requests.value = it.toList()
            }
        }
    }

    /**
     * Пытается выполнить все запросы.
     */
    fun retryHandleRequests() {
        viewModelScope.launch {
            _executeState.emit(ExecuteStates.INPROGRESS)
            if (deferredRequestsService.retryHandleRequests()) {
                _executeState.emit(ExecuteStates.SUCCESS)
            } else {
                Toast.makeText(DeferredRequestApplication.application, "Отправка не удалась, попробуйте позже", Toast.LENGTH_LONG).show()
                _executeState.emit(ExecuteStates.NONE)
            }
        }
    }

    /**
     * Удаляет все запросы из кэша.
     */
    fun removeAllRequests() {
        deferredRequestStorage.removeAllRequestsModel()
    }
}