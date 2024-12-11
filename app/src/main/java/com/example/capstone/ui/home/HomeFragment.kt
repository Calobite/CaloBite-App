package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.NewsAdapter
import com.example.capstone.data.ViewModelFactory
import com.example.capstone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var newsAdapter: NewsAdapter

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setRecyclerView()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"

        viewModel.getCancerArticles()

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        viewModel.newsList.observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.updateNewsList(articles)
        })

        return root
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvNews.addItemDecoration(itemDecoration)

        newsAdapter = NewsAdapter()
        binding.rvNews.adapter = newsAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}