package com.example.expenseutility.adapter

import android.content.Context
import android.graphics.Color
import android.net.ParseException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.expenseutility.R
import com.example.expenseutility.model.ExpenseDaily
import java.text.SimpleDateFormat
import java.util.*

class MyGridAdapter(
    context: Context,
    dates: MutableList<Date>?,
    calendar: Calendar,
    events: MutableList<ExpenseDaily>?,
    attendanceList: MutableList<String>?
) : ArrayAdapter<Any>(context, R.layout.day_of_the_month_layout) {
    var dates: MutableList<Date>? = ArrayList()

    var events: MutableList<ExpenseDaily>? = ArrayList()
    private var attendanceList: MutableList<String>? = ArrayList()

    var calendar: Calendar? = calendar
    var inflater: LayoutInflater? = null

    var back: RelativeLayout? = null
    private var dayNumber: TextView? = null
    private var circleImage1: ImageView? = null
    private var circleImage2: ImageView? = null
    private var circleImage3: ImageView? = null
    private var circleImage4: ImageView? = null

    init {
        this.dates = dates
        this.calendar = calendar
        this.events = events
        this.attendanceList = attendanceList
        inflater = LayoutInflater.from(context)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val monthDate = dates!![position]
        val dateCalendar = Calendar.getInstance(Locale.ENGLISH)
        dateCalendar.time = monthDate
        val dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val displayMonth = dateCalendar.get(Calendar.MONTH) + 1
        val displayYear = dateCalendar.get(Calendar.YEAR)
        val currentMonth = calendar!!.get(Calendar.MONTH) + 1
        val currentYear = calendar!!.get(Calendar.YEAR)

        var view = convertView
        if (view == null) {
            view = inflater!!.inflate(R.layout.day_of_the_month_layout, null)
        }
        back = view!!.findViewById(R.id.expense_background)
        dayNumber = view.findViewById(R.id.expense_date_no)
        circleImage1 = view.findViewById(R.id.dayOfTheMonthCircleImage1)
        circleImage2 = view.findViewById(R.id.dayOfTheMonthCircleImage2)
        circleImage3 = view.findViewById(R.id.dayOfTheMonthCircleImage3)
        circleImage4 = view.findViewById(R.id.dayOfTheMonthCircleImage4)

        back!!.setBackgroundResource(android.R.color.transparent)

        circleImage1!!.visibility = View.GONE
        circleImage2!!.visibility = View.GONE
        circleImage3!!.visibility = View.GONE
        circleImage4!!.visibility = View.GONE

        if (currentMonth == displayMonth && currentYear == displayYear) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }


        /**Mark Attendance on Calender*/
        val attendanceCalendar = Calendar.getInstance(Locale.ENGLISH)

        for (i in 0 until attendanceList!!.size) {
            attendanceCalendar.time = convertStringToDate(attendanceList!![i])
            if (dayNo == attendanceCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == attendanceCalendar.get(
                    Calendar.MONTH
                ) + 1 && displayYear == attendanceCalendar.get(Calendar.YEAR)
            ) {
                circleImage1!!.visibility = View.VISIBLE
            }
        }


        /**Mark Expense on Calender*/
        dayNumber!!.text = dayNo.toString()
        val eventCalendar = Calendar.getInstance(Locale.ENGLISH)

        for (i in 0 until events!!.size) {
            eventCalendar.time = convertStringToDate(events!![i].date.toString())
            if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(
                    Calendar.MONTH
                ) + 1 && displayYear == eventCalendar.get(Calendar.YEAR) && events!![i].status == "Pending"
            ) {
                circleImage2!!.visibility = View.VISIBLE
                circleImage1!!.visibility = View.GONE
            } else if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(
                    Calendar.MONTH
                ) + 1 && displayYear == eventCalendar.get(Calendar.YEAR) && (events!![i].status != "Pending" || events!![i].status != "Processed")
            ) {
                circleImage3!!.visibility = View.VISIBLE
                circleImage1!!.visibility = View.GONE
            } else if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(
                    Calendar.MONTH
                ) + 1 && displayYear == eventCalendar.get(Calendar.YEAR) && events!![i].status == "Processed"
            ) {
                circleImage4!!.visibility = View.VISIBLE
                circleImage1!!.visibility = View.GONE
            }

        }

        /** Mark Sunday */

        if (dateCalendar.get(Calendar.DAY_OF_WEEK) == 1) {
            dayNumber!!.setTextColor(Color.parseColor("#96000F"))
        }


        /** Mark Current date on Calender */

        if (Calendar.getInstance()[Calendar.YEAR] == dateCalendar.get(Calendar.YEAR) && Calendar.getInstance()[Calendar.MONTH] == dateCalendar.get(
                Calendar.MONTH
            ) && SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(dateCalendar.time) == SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
        ) {
            back!!.setBackgroundResource(R.drawable.ring)
        }

        return view
    }

    private fun convertStringToDate(eventDate: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var date: Date? = null
        try {
            date = format.parse(eventDate)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date as Date
    }


    override fun getCount(): Int {
        return dates!!.size
    }

    override fun getPosition(item: Any?): Int {
        return dates!!.indexOf(item)
    }

    override fun getItem(position: Int): Any {
        return dates!![position]
    }
}