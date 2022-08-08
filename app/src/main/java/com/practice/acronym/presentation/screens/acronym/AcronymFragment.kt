package com.practice.acronym.presentation.screens.acronym

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.acronym.R
import com.practice.acronym.data_layer.model.Acronym
import com.practice.acronym.data_layer.network.AcronymRepository
import com.practice.acronym.data_layer.network.Resource
import com.practice.acronym.data_layer.network.Status
import com.practice.acronym.databinding.FragmentAcronymBinding
import com.practice.acronym.domain_layer.utils.launchAndCollectIn
import com.practice.acronym.domain_layer.utils.toastMsg
import kotlinx.coroutines.launch

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class AcronymFragment : Fragment() {
    lateinit var binding: FragmentAcronymBinding
    private lateinit var viewModel: AcromineViewModel
    private val meaningAdapter: AcronymMeaningAdapter by lazy {
        AcronymMeaningAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAcronymBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = AcronymViewModelFactory()
        viewModel = ViewModelProvider(this, repository)[AcromineViewModel::class.java]
        binding.apply {
            searchView.apply {
                isActivated = true
                onActionViewExpanded()
                setOnQueryTextListener(meaningSearchListener)
            }
            meaningRv.apply {
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                adapter = meaningAdapter
            }
        }
        viewModel.meaningFlow.launchAndCollectIn(viewLifecycleOwner) {
            updateView(it)
        }

    }

    private fun updateView(res: Resource<List<Acronym>>) {
        when (res.status) {
            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                res.data?.takeIf { it.isNotEmpty() }?.let {
                    it[0].let {
                        it.lfs?.let {
                            meaningAdapter.setData(it)
                        } ?: kotlin.run {
                            activity?.let { toastMsg(it, getString(R.string.no_data)) }
                        }
                    }
                } ?: kotlin.run {
                    activity?.let { toastMsg(it, getString(R.string.no_data)) }
                }
            }
            Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                activity?.let { toastMsg(it, getString(R.string.err_admin)) }
            }
        }
    }

    private val meaningSearchListener by lazy {
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(acronymInput: String?): Boolean {
                meaningAdapter.setData(emptyList())
                acronymInput?.length?.takeIf { it > 0 }?.let {
                    viewModel.getMeaningsFlow(acronymInput)
                } ?: kotlin.run {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_acronym),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }
        }
    }

    companion object {
        fun newInstance() = AcronymFragment()
    }
}
