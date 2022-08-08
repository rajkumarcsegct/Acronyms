package com.practice.acronym.presentation.screens.acronym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.acronym.data_layer.APIServices.AcronymAPI
import com.practice.acronym.data_layer.network.APIClient
import com.practice.acronym.data_layer.network.AcronymRepository
import kotlinx.coroutines.Dispatchers

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class AcronymViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcromineViewModel::class.java)) {
            return AcromineViewModel(AcronymRepository(APIClient.createService(tClass = AcronymAPI::class.java))) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}