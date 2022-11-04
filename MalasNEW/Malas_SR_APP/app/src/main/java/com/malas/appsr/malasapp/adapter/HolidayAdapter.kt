package com.malas.appsr.malasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malas.appsr.malasapp.R
import java.text.SimpleDateFormat
import java.util.*

class HolidayAdapter(holidayDate: MutableList<String>?, event: HashMap<String, String>?) : RecyclerView.Adapter<HolidayAdapter.ViewHolder>() {

    var holidayDate: MutableList<String>? = null
    var event: HashMap<String, String>? = null

    init {
        this.holidayDate = holidayDate
        this.event = event
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holiday_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return holidayDate!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date!!.text = SimpleDateFormat("dd", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(holidayDate!![position]) as Date)
        holder.day!!.text = SimpleDateFormat("EEE", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(holidayDate!![position]) as Date)
        holder.desc!!.text = event!![holidayDate!![position]]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView? = null
        var day: TextView? = null
        var desc: TextView? = null

        init {
            date = itemView.findViewById(R.id.holiday_date)
            day = itemView.findViewById(R.id.holiday_day)
            desc = itemView.findViewById(R.id.holiday_desc)
        }
    }

}