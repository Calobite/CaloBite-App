package com.example.capstone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.databinding.ItemFoodBinding

class FoodAdapter(private val onItemClick: (ListDataFood) -> Unit) :
    PagingDataAdapter<ListDataFood, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val food = getItem(position)
        food?.let {
            holder.bind(it)
        }
    }

    inner class MyViewHolder(
        private val binding: ItemFoodBinding,
        private val onItemClick: (ListDataFood) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: ListDataFood) {
            binding.tvFoodTitle.text = food.recipeName
            binding.rating.rating = food.rating ?: 0.0f
            Glide.with(binding.root.context)
                .load(food.imgSrc)
                .into(binding.imgFood)

            binding.root.setOnClickListener { onItemClick(food) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListDataFood>() {
            override fun areItemsTheSame(
                oldItem: ListDataFood,
                newItem: ListDataFood
            ): Boolean {
                return oldItem.recipesId == newItem.recipesId // Assuming 'id' is unique for each food item
            }

            override fun areContentsTheSame(
                oldItem: ListDataFood,
                newItem: ListDataFood
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}



