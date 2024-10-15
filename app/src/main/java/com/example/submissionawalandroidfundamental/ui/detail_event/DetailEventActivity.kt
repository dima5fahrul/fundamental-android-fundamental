package com.example.submissionawalandroidfundamental.ui.detail_event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import com.example.submissionawalandroidfundamental.databinding.ActivityDetailEventBinding
import com.example.submissionawalandroidfundamental.utils.DataHelper

class DetailEventActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityDetailEventBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_detail_event)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val event = intent.getParcelableExtra<EventEntity>("listEvents")

        event?.let { it: EventEntity ->
            Glide.with(this)
                .load(it.mediaCover)
                .into(_binding.imgDetailPhoto)
            _binding.tvDetailTitle.text = it.name
            _binding.tvDetailOwnerName.text = it.ownerName
            _binding.tvDetailCityName.text = it.cityName
            _binding.tvDetailCategory.text = it.category
            _binding.tvDetailSummary.text = it.summary
            _binding.tvDetailDescription.text = HtmlCompat.fromHtml(
                it.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            _binding.tvDetailBeginTime.text = DataHelper
                .convertDate(it.beginTime.toString())
            val quota = it.quota
            val registrants = it.registrants
            val remainingQuota = quota!! - registrants!!
            _binding.tvDetailQuota.text = remainingQuota.toString()
            val linked = it.link
            if (!linked.isNullOrEmpty()) {
                _binding.btnDetailRegister.text = getString(R.string.daftar_sekarang)
                _binding.btnDetailRegister.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linked))
                    startActivity(intent)
                }
            } else {
                _binding.btnDetailRegister.text = getString(R.string.link_tidak_tersedia)
                _binding.btnDetailRegister.isEnabled = false
            }
        }

        _binding.btnDetailRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link))
            startActivity(intent)
        }
    }
}