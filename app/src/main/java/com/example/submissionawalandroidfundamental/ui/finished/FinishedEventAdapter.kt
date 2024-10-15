package com.example.submissionawalandroidfundamental.ui.finished

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import com.example.submissionawalandroidfundamental.databinding.ItemListBinding
import com.example.submissionawalandroidfundamental.ui.detail_event.DetailEventActivity

class FinishedEventAdapter(private val onBookmarkClick: (EventEntity) -> Unit) :
    ListAdapter<EventEntity, FinishedEventAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivBookmark = holder.binding.ivBookmark
        if (event.isBookmarked) {
            ivBookmark.setImageDrawable(
                ivBookmark.context.getDrawable(
                    R.drawable.ic_bookmarked_black
                )
            )
        } else {
            ivBookmark.setImageDrawable(
                ivBookmark.context.getDrawable(
                    R.drawable.ic_bookmark_black
                )
            )
        }
        ivBookmark.setOnClickListener { onBookmarkClick(event) }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: EventEntity,
                    newItem: EventEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}