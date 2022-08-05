package com.practice.acronym.presentation.screens.acronym

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.acronym.databinding.MeaningsItemViewBinding
import com.practice.acronym.data_layer.model.Meanings
import com.practice.acronym.presentation.screens.acronym.AcronymMeaningAdapter.MeaningVH
import com.practice.acronym.domain_layer.utils.capitalizeWord

class AcronymMeaningAdapter : RecyclerView.Adapter<MeaningVH>() {

    private var meanings: List<Meanings> = ArrayList()

    inner class MeaningVH(private val itemBinding: MeaningsItemViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun updateView(meaning: Meanings?) {
            meaning?.let {
                itemBinding.txtTitle.text = it.lf?.trim()?.capitalizeWord()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningVH {
        val binding =
            MeaningsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeaningVH(binding)
    }

    override fun onBindViewHolder(holder: MeaningVH, position: Int) {
        holder.updateView(meanings.get(position))
    }

    fun setData(data: List<Meanings>) {
        meanings = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return meanings.size
    }
}