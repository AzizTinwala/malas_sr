package com.example.expenseutility.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseutility.model.ExpenseDaily
import com.example.expenseutility.R
import java.text.SimpleDateFormat
import java.util.*


class ExpensePreviewtAdapter(
    daily_expense_list: MutableList<ExpenseDaily>?
) :
    RecyclerView.Adapter<ExpensePreviewtAdapter.ViewHolder>() {
    var context: Context? = null
    var daily_expense_list: MutableList<ExpenseDaily>? = null


    init {
        this.daily_expense_list = daily_expense_list

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.expense_preview_items, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daily_expense_list!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.date!!.text = SimpleDateFormat("dd", Locale.getDefault()).format(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(daily_expense_list!![position].date as String) as Date
        )
        holder.town!!.text = daily_expense_list!![position].townFrom!!
            .uppercase(Locale.getDefault()) + "\nTO\n" + daily_expense_list!![position].townTo!!
            .uppercase(Locale.getDefault())

        holder.da!!.text = daily_expense_list!![position].da.toString()



        holder.ta!!.text = daily_expense_list!![position].ta.toString()
        holder.inout!!.text = daily_expense_list!![position].inOut.toString()
        holder.other!!.text = daily_expense_list!![position].other.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView? = null
        var town: TextView? = null
        var da: TextView? = null
        var ta: TextView? = null
        var inout: TextView? = null
        var other: TextView? = null

        init {
            date = itemView.findViewById(R.id.expense_item_date)
            town = itemView.findViewById(R.id.expense_item_town)
            da = itemView.findViewById(R.id.expense_item_da)
            ta = itemView.findViewById(R.id.expense_item_ta)
            inout = itemView.findViewById(R.id.expense_item_inout)
            other = itemView.findViewById(R.id.expense_item_other)
        }
    }
}