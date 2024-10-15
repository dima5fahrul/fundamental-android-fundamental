package com.example.submissionawalandroidfundamental.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.models.EventModel
import com.example.submissionawalandroidfundamental.ui.detail_event.DetailEventActivity

class HomeUpcomingAdapter(private val listReview: ArrayList<EventModel>) :
    RecyclerView.Adapter<HomeUpcomingAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPhoto: ImageView = view.findViewById(R.id.img_item_photo)
    }

    override fun getItemCount(): Int = if (listReview.size > 5) 5 else listReview.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = listReview[position]
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailEventActivity::class.java).apply {
                putExtra("listEvents", event)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }
}