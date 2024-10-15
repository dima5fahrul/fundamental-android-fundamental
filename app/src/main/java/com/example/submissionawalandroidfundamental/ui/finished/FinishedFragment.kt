package com.example.submissionawalandroidfundamental.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.databinding.FragmentFinishedBinding
import com.example.submissionawalandroidfundamental.models.EventModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FinishedViewModel

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

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FinishedViewModel::class.java]
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            val adapter = FinishedEventAdapter(events as ArrayList<EventModel>)
            binding.rvUpcoming.adapter = adapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner, ::showLoading)

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.rvUpcoming)
                    .show()
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.loadFinishedEvents(textView.text.toString())
                    true
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}