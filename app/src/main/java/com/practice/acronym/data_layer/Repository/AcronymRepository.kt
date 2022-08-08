package com.practice.acronym.data_layer.network

import com.practice.acronym.data_layer.APIServices.AcronymAPI
import com.practice.acronym.data_layer.model.Acronym
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class AcronymRepository(private val acronymAPI: AcronymAPI) {

    suspend fun fetchMeanings(sform: String): Flow<Response<List<Acronym>>> {
        return flow {
            val meanings = acronymAPI.getAcr(sform)
            emit(meanings)
        }
    }

}