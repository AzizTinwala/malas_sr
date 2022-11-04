package com.example.expenseutility

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.example.expenseutility.model.ExpenseDaily
import com.example.expenseutility.adapter.MyGridAdapter
import java.text.SimpleDateFormat
import java.util.*

class ExpenseManagement : LinearLayout {

    var previous: ImageView? = null
    var next: ImageView? = null
    var current_month: TextView? = null
    var gridView: GridView? = null

    val MAX_CALENDAR_DAYS: Int = 42
    val cal = Calendar.getInstance()

    var mygridadapter: MyGridAdapter? = null
    var alertDialog: AlertDialog? = null
    var dates: MutableList<Date>? = ArrayList()
    var eventsList: MutableList<ExpenseDaily>? = ArrayList()
    var attendanceList: MutableList<String>? = ArrayList()

    val wt = arrayOf(
        "Head Quarter(HQ)", "Up Country(UC)", "Night Halt(NH)","Team Meeting(TM)","Work From Home(WFH)"
    )
    var arrayadapter: ArrayAdapter<String>? = null

    constructor(context: Context) : super(context)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.calendar_layout, this)
        next = view.findViewById(R.id.expense_rightButton)
        previous = view.findViewById(R.id.expense_leftButton)
        current_month = view.findViewById(R.id.expense_monthText)
        gridView = findViewById(R.id.gridview)
        setupCalendar()


    }

    fun setupCalendar() {
        current_month!!.text =
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time).toString()
        dates!!.clear()
        val monthCalendar: Calendar = cal.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth)
        // CollectEventsPerMonth(monthFormat.format(calendar.time), yearFormat.format(calendar.time))

        while (dates!!.size < MAX_CALENDAR_DAYS) {
            dates!!.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        mygridadapter = MyGridAdapter(context, dates, cal, eventsList,attendanceList)
        gridView!!.adapter = mygridadapter!!
    }

    fun setEvents(dailyExpenseList: MutableList<ExpenseDaily>?) {
        eventsList = dailyExpenseList
        setupCalendar()
    }

    fun setAttendance(data: MutableList<String>?) {
        attendanceList = data
        setupCalendar()

    }


}