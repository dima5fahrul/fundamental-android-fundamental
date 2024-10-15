package com.example.submissionawalandroidfundamental.ui.finished

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.models.EventModel
import com.example.submissionawalandroidfundamental.ui.detail_event.DetailEventActivity
import com.example.submissionawalandroidfundamental.utils.DataHelper

class FinishedEventAdapter(private val listReview: ArrayList<EventModel>) :
    RecyclerView.Adapter<FinishedEventAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPhoto: ImageView = view.findViewById(R.id.img_item_photo)
        val tvName: TextView = view.findViewById(R.id.tv_item_name)
        val tvEndTime: TextView = view.findViewById(R.id.tv_item_begin_time)
        val tvCityName: TextView = view.findViewById(R.id.tv_item_city_name)
    }

    override fun getItemCount(): Int = listReview.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = listReview[position]
        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.imgPhoto)
        holder.tvName.text = event.name
        holder.tvEndTime.text = DataHelper.convertDate(event.endTime.toString())
        holder.tvCityName.text = event.cityName

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailEventActivity::class.java).apply {
                putExtra("listEvents", event)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }
}