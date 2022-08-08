package com.practice.acronym

import androidx.lifecycle.*
import com.practice.acronym.data_layer.APIServices.AcronymAPI
import com.practice.acronym.data_layer.model.Acronym
import com.practice.acronym.data_layer.model.Meanings
import com.practice.acronym.data_layer.network.AcronymRepository
import com.practice.acronym.presentation.screens.acronym.AcromineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class AcromineViewModelTest {

    private lateinit var viewModel: AcromineViewModel

    @Mock
    private lateinit var acronymAPI: AcronymAPI

    @Mock
    private lateinit var repository: AcronymRepository


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = AcromineViewModel(AcronymRepository(acronymAPI))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_IsDataAvailable() = runTest {

        val meanings = Meanings(lf = "World Wide Web")
        val retroResponse = Acronym(listOf(meanings))
        val response = retrofit2.Response.success(listOf(retroResponse))
        val channel = Channel<retrofit2.Response<List<Acronym>>>()
        val flow = channel.consumeAsFlow()

        Mockito.`when`(repository.fetchMeanings("WWW")).thenReturn(flow)

        launch {
            channel.send(response)
        }

        viewModel.getMeaningsFlow("WWW")

        viewModel.meaningFlow.last().data?.takeIf { it.isNotEmpty() }?.let {
            assert(true)
        } ?: assert(false)


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_IsMeaningAvailable() = runTest {

        val meanings = Meanings(lf = "World Wide Web")
        val retroResponse = Acronym(listOf(meanings))
        val response = retrofit2.Response.success(listOf(retroResponse))
        val channel = Channel<retrofit2.Response<List<Acronym>>>()
        val flow = channel.consumeAsFlow()

        Mockito.`when`(repository.fetchMeanings("WWW")).thenReturn(flow)

        launch {
            channel.send(response)
        }

        viewModel.getMeaningsFlow("WWW")

        viewModel.meaningFlow.last().data?.takeIf { it.isNotEmpty() }?.let {
            it[0].lfs?.let {
                assert(true)
            } ?: assert(false)

        }

    }

}


