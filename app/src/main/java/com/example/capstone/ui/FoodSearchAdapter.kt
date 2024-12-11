package com.example.capstone.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.data.response.ListDataFood
import com.example.capstone.databinding.ItemFoodBinding

class FoodSearchAdapter(
    private val foodList: List<ListDataFood>,  // Terima List<ListDataFood> di constructor
    private val onClick: (ListDataFood) -> Unit // Lambda untuk menangani item yang diklik
) : RecyclerView.Adapter<FoodSearchAdapter.FoodViewHolder>() {

    // Inner class untuk ViewHolder
    inner class FoodViewHolder(
        private val binding: ItemFoodBinding,
        private val onItemClick: (ListDataFood) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: ListDataFood) {
            // Menampilkan data pada ViewBinding
            binding.tvFoodTitle.text = food.recipeName // Menampilkan nama makanan
            binding.rating.rating = food.rating?.toFloat() ?: 0.0f // Menampilkan rating
            Glide.with(binding.root.context)
                .load(food.imgSrc) // Memuat gambar menggunakan Glide
                .into(binding.imgFood)

            // Menangani klik item
            binding.root.setOnClickListener { onItemClick(food) }
        }
    }

    // Implementasi onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        // Inflate layout item_food.xml menggunakan ViewBinding
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding, onClick) // Mengembalikan FoodViewHolder dengan binding dan lambda onClick
    }

    // Implementasi onBindViewHolder
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.bind(food) // Bind data food ke ViewHolder
    }

    // Mengembalikan jumlah item yang ada
    override fun getItemCount(): Int = foodList.size
}
