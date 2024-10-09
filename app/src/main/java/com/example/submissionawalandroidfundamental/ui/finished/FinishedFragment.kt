package com.example.submissionawalandroidfundamental.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.databinding.FragmentFinishedBinding
import com.example.submissionawalandroidfundamental.models.FinishedEventModel

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

        viewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            val adapter = FinishedEventAdapter(events as ArrayList<FinishedEventModel>)
            binding.rvUpcoming.adapter = adapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadFinishedEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}