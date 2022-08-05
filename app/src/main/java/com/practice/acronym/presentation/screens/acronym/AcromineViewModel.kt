package com.practice.acronym.presentation.screens.acronym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.acronym.data_layer.APIServices.AcronymAPI
import com.practice.acronym.data_layer.model.Acronym
import com.practice.acronym.data_layer.network.APIClient
import com.practice.acronym.data_layer.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AcromineViewModel : ViewModel() {

    var acrSearchJob: Job? = null

    private val _meaningFlow = MutableSharedFlow<Resource<List<Acronym>>>()
    val meaningFlow = _meaningFlow as SharedFlow<Resource<List<Acronym>>>

    /*fun getMeanings(acro: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _meaningStateFlow.emit(Resource.loading(null))
                val meanObj =
                    APIClient.createService(tClass = AcronymAPI::class.java)
                        .getAcr(acro)
                meanObj.takeIf { it.isSuccessful }?.let {
                    _meaningStateFlow.emit(Resource.success(it.body() as ArrayList<Acronym>))
                } ?: kotlin.run {
                    _meaningStateFlow.emit(Resource.error("Error", null))
                }
            } catch (e: Exception) {
                _meaningStateFlow.value = Resource.error(e.message ?: "Err", null)
            }
        }
    }*/

    fun getMeaningsFlow(searchAbbr: String) {
        acrSearchJob?.cancel()
        acrSearchJob = viewModelScope.launch {
            _meaningFlow.emit(Resource.loading(null))
            getFlowStatus(searchAbbr)
                .catch { e ->
                    e.message?.let {
                        _meaningFlow.emit(Resource.error(it))
                    }
                }.flowOn(Dispatchers.IO)
                .collectLatest {
                    it.takeIf { it.isSuccessful }?.let {
                        _meaningFlow.emit(Resource.success(it.body() as ArrayList<Acronym>))
                    } ?: kotlin.run {
                        _meaningFlow.emit(Resource.error("Error", null))
                    }
                }
        }

    }

    private fun getFlowStatus(searchAbbr: String) = flow {
        emit(APIClient.createService(tClass = AcronymAPI::class.java).getAcr(searchAbbr))
    }
}
