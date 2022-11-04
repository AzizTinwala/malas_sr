package com.malas.appsr.malasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseutility.model.ExpenseDaily
import com.malas.appsr.malasapp.R
import java.text.SimpleDateFormat
import java.util.*

class ExpenseDailyAdapter(daily_expense_list: MutableList<ExpenseDaily>) :
    RecyclerView.Adapter<ExpenseDailyAdapter.ViewHolder>() {

    var dailyExpenseList: MutableList<ExpenseDaily>? = null

    init {
        this.dailyExpenseList = daily_expense_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate( R.layout.holiday_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dailyExpenseList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date!!.text =
            SimpleDateFormat("dd", Locale.getDefault()).format(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(dailyExpenseList!![position].date.toString()) as Date
            )
        holder.day!!.text =
            SimpleDateFormat("EEE", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(dailyExpenseList!![position].date.toString())as Date)
        holder.desc!!.text =String.format(dailyExpenseList!![position].townFrom + " - " + dailyExpenseList!![position].townTo)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView? = null
        var day: TextView? = null
        var desc: TextView? = null

        init {
            date = itemView.findViewById(R.id.holiday_date)
            day = itemView.findViewById(R.id.holiday_day)
            desc = itemView.findViewById(R.id.holiday_desc)
            date!!.textSize = 12.0f
            day!!.textSize = 10.0f
            desc!!.textSize = 15.0f
        }
    }

}