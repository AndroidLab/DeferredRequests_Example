package com.alab.deferredrequests_example

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Представляет хранилище для отложенных запросов.
 */
class DeferredRequestStorage : IDeferredRequestStorage {
    private val _requests = mutableListOf<IDeferredRequestModel>()
    private val _requestsFlow = MutableSharedFlow<List<IDeferredRequestModel>>(extraBufferCapacity = 1, replay = 1)

    override val requestsFlow: SharedFlow<List<IDeferredRequestModel>> = _requestsFlow.asSharedFlow()

    override val countElements: Int
        get() = _requests.size

    override fun getRequestModel(position: Int) =
        if(position > -1 && position < countElements) {
            _requests[position]
        } else {
            null
        }

    override fun addRequestModel(requestModel: IDeferredRequestModel): Boolean {
        return if (_requests.contains(requestModel)) {
            false
        } else {
            _requests.add(requestModel)
            _requestsFlow.tryEmit(_requests)
            true
        }
    }

    override fun removeRequestModel(requestModel: IDeferredRequestModel) {
        _requests.remove(requestModel)
        _requestsFlow.tryEmit(_requests)
    }

    override fun removeAllRequestsModel() {
        _requests.clear()
        _requestsFlow.tryEmit(_requests)
    }
}