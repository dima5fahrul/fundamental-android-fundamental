package com.example.submissionawalandroidfundamental.ui.finished

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
import com.example.submissionawalandroidfundamental.databinding.FragmentFinishedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        val bottomNavHeight = bottomNav.height
        binding.rvUpcoming.setPadding(0, 0, 0, bottomNavHeight * 3)

        val factory: FinishedViewModelFactory =
            FinishedViewModelFactory.getInstance(requireActivity())
        val viewModel: FinishedViewModel by viewModels { factory }

        val finishedAdapter = FinishedEventAdapter { event ->
            if (event.isBookmarked) {
                viewModel.deleteEvent(event)
            } else {
                viewModel.saveEvent(event, true)
            }
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }

        if (finishedAdapter.itemCount == 0) {
            viewModel.getFinishedEvents(null).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            finishedAdapter.submitList(result.data)
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
                    viewModel.getFinishedEvents(textView.text.toString())
                    true
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}