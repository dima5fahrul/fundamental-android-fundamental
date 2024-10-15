package com.example.submissionawalandroidfundamental.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: FavoriteViewModelFactory =
            FavoriteViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteViewModel by viewModels { factory }

        val favoriteAdapter = FavoriteAdapter { event -> viewModel.deleteEvent(event) }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        viewModel.getBookmarkedEvents().observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            favoriteAdapter.submitList(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}