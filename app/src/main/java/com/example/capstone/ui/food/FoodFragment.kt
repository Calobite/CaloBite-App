package com.example.capstone.ui.food

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.FoodAdapter
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.databinding.FragmentFoodBinding
import com.example.capstone.ui.detailFood.DetailFoodActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest


class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private lateinit var foodAdapter: FoodAdapter
    private val binding get() = _binding!!

    private val viewModel by viewModels<FoodViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Food Recipe"

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foodStream.collectLatest { pagingData ->
                foodAdapter.submitData(pagingData) // Submitting the paging data to the adapter
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.fetchDataFood(page = 0, size = 0)
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFood.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFood.addItemDecoration(itemDecoration)

        foodAdapter = FoodAdapter { food ->
            val intent = Intent(requireActivity(), DetailFoodActivity::class.java)
            intent.putExtra("FOOD_DATA", food)
            startActivity(intent)
        }
        binding.rvFood.adapter = foodAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
