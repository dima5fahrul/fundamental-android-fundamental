package com.example.submissionawalandroidfundamental.ui.detail_event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.submissionawalandroidfundamental.databinding.FragmentDetailEventBinding
import com.example.submissionawalandroidfundamental.models.EventModel

class DetailEventFragment : Fragment() {
    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = arguments?.getParcelable<EventModel>("listEvents")

        event?.let {
            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.imgDetailPhoto)
            binding.tvDetailTitle.text = it.name
            binding.tvDetailOwnerName.text = it.ownerName
            binding.tvDetailCityName.text = it.cityName
            binding.tvDetailCategory.text = it.category
            binding.tvDetailDescription.text = it.description
            binding.tvDetailBeginTime.text = it.beginTime
            val quota = it.quota.toString()
            val registrants = it.registrants.toString()
            binding.tvDetailQuotaRegistrant.text = "$quota - $registrants"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}