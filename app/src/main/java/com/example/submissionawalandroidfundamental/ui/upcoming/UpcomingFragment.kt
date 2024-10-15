package com.example.submissionawalandroidfundamental.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.data.Result
import com.example.submissionawalandroidfundamental.databinding.FragmentUpcomingBinding
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: UpcomingViewModelFactory =
            UpcomingViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels { factory }

        val upcomingAdapter = UpcomingAdapter { event ->
            if (event.isBookmarked) {
                viewModel.deleteEvent(event)
            } else {
                viewModel.saveEvent(event, true)
            }
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingAdapter
        }

        if (upcomingAdapter.itemCount == 0) {
            viewModel.getUpcomingEvents(null).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            upcomingAdapter.submitList(result.data)
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(
                                requireView(),
                                getString(R.string.terjadi_kesalahan), Snackbar.LENGTH_SHORT
                            )
                                .setAnchorView(binding.rvUpcoming)
                                .show()
                        }
                    }
                } else {
                    Log.d("UpcomingFragment", "Result is null")
                    Snackbar.make(
                        requireView(), getString(R.string.result_is_null), Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(binding.rvUpcoming)
                        .show()
                }
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getUpcomingEvents(textView.text.toString())
                    true
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}