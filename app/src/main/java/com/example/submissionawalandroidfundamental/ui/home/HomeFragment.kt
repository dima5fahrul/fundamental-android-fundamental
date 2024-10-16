package com.example.submissionawalandroidfundamental.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.data.SettingPreferences
import com.example.submissionawalandroidfundamental.data.dataStore
import com.example.submissionawalandroidfundamental.databinding.FragmentHomeBinding
import com.example.submissionawalandroidfundamental.databinding.FragmentSettingsBinding
import com.example.submissionawalandroidfundamental.models.EventModel
import com.example.submissionawalandroidfundamental.ui.settings.SettingsViewModel
import com.example.submissionawalandroidfundamental.ui.settings.SettingsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var _bindingSetting: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val bindingSetting get() = _bindingSetting!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _bindingSetting = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        val bottomNavHeight = bottomNav.height
        binding.rvVerticalFinished.setPadding(0, 0, 0, bottomNavHeight * 5)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel: SettingsViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(pref)
        )[SettingsViewModel::class.java]
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        val switchTheme = bindingSetting.swDarkMode
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        binding.rvHorizontalUpcoming.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvVerticalFinished.layoutManager = LinearLayoutManager(context)

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            val adapter = HomeUpcomingAdapter(events as ArrayList<EventModel>)
            binding.rvHorizontalUpcoming.adapter = adapter
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            val adapter = HomeFinishedAdapter(events as ArrayList<EventModel>)
            binding.rvVerticalFinished.adapter = adapter
        }

        viewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorUpcoming.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorFinished.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}