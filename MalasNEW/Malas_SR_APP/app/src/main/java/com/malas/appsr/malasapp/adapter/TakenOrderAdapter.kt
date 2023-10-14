package com.malas.appsr.malasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malas.appsr.malasapp.BeanClasses.TakenOrderBean
import com.malas.appsr.malasapp.R
import java.util.*
import kotlin.collections.ArrayList

class TakenOrderAdapter(var orderItem: MutableList<TakenOrderBean>?) :
    RecyclerView.Adapter<TakenOrderAdapter.ViewHolder>(),Filterable {
    var orderItemAll: MutableList<TakenOrderBean>?=null
   init {
        this.orderItemAll=orderItem?.let { ArrayList(it) }
   }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.taken_order_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.product_name!!.text = orderItem!![position].skuName
        holder.product_category!!.text = orderItem!![position].categoryName
        holder.product_quantity!!.text = orderItem!![position].orderQty
    }

    override fun getItemCount(): Int {
        return orderItem!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product_name: TextView? = null
        var product_category: TextView? = null
        var product_quantity: TextView? = null

        init {
            product_name = itemView.findViewById(R.id.product_name)
            product_category = itemView.findViewById(R.id.product_category)
            product_quantity = itemView.findViewById(R.id.product_quantity)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterList: MutableList<TakenOrderBean> = ArrayList()

            if (constraint.toString().isEmpty()) {
                filterList.addAll(orderItemAll!!)

            } else {
                val temp = orderItemAll
                temp!!.forEach {
                    if (it.skuName!!.lowercase(Locale.getDefault())
                            .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ||it.categoryName!!.lowercase(Locale.getDefault())
                            .contains(constraint.toString().lowercase(Locale.getDefault()))
                    ) {
                        filterList.add(it)
                    }

                }
            }


            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            orderItem!!.clear()
            orderItem!!.addAll(results!!.values as Collection<TakenOrderBean>)
            notifyDataSetChanged()
        }

    }
    }
}