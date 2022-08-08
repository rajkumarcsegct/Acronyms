package com.practice.acronym.presentation.screens.acronym

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.acronym.data_layer.APIServices.AcronymAPI
import com.practice.acronym.data_layer.model.Acronym
import com.practice.acronym.data_layer.network.APIClient
import com.practice.acronym.data_layer.network.AcronymRepository
import com.practice.acronym.data_layer.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class AcromineViewModel(private val repository: AcronymRepository) : ViewModel() {

    var acrSearchJob: Job? = null

    private val _meaningFlow = MutableSharedFlow<Resource<List<Acronym>>>()
    val meaningFlow = _meaningFlow as SharedFlow<Resource<List<Acronym>>>

    /**
     * Get meaning for the given acronym and emit it to view
     */
    fun getMeaningsFlow(searchAbbr: String) {
        acrSearchJob?.cancel()
        acrSearchJob = viewModelScope.launch {
            _meaningFlow.emit(Resource.loading(null))
            repository.fetchMeanings(searchAbbr)
                .catch { e ->
                    e.message?.let {
                        _meaningFlow.emit(Resource.error(it))
                    }
                }.flowOn(Dispatchers.IO)
                .collectLatest {
                    it.takeIf { it.isSuccessful }?.let {
                        _meaningFlow.emit(Resource.success(it.body() as ArrayList<Acronym>))
                    } ?: kotlin.run {
                        _meaningFlow.emit(Resource.error())
                    }
                }
        }

    }
}
