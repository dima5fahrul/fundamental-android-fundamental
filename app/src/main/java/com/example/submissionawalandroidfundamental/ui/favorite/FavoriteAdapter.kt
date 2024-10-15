package com.example.submissionawalandroidfundamental.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import com.example.submissionawalandroidfundamental.databinding.ItemListBinding
import com.example.submissionawalandroidfundamental.ui.detail_event.DetailEventActivity
import com.example.submissionawalandroidfundamental.ui.upcoming.UpcomingAdapter.Companion.DIFF_CALLBACK

class FavoriteAdapter(private val onBookmarkClick: (EventEntity) -> Unit) :
    ListAdapter<EventEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.tvItemName.text = event.name
            binding.tvItemCityName.text = event.cityName
            binding.tvItemBeginTime.text = event.beginTime
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_refresh_black)
                        .error(R.drawable.ic_error)
                )
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailEventActivity::class.java)
                intent.putExtra("listEvents", event)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivBookmark = holder.binding.ivBookmark
        if (event.isBookmarked) {
            ivBookmark.setImageDrawable(
                ivBookmark.context.getDrawable(R.drawable.ic_bookmarked_black)
            )
        } else {
            ivBookmark.setImageDrawable(
                ivBookmark.context.getDrawable(R.drawable.ic_bookmark_black)
            )
        }
        ivBookmark.setOnClickListener { onBookmarkClick(event) }
    }
}