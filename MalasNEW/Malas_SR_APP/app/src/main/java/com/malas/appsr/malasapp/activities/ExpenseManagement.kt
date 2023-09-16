package com.malas.appsr.malasapp.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Amitlibs.utils.ComplexPreferences
import com.bumptech.glide.Glide
import com.example.expenseutility.ExpenseManagement
import com.example.expenseutility.adapter.ExpensePreviewtAdapter
import com.example.expenseutility.model.ExpenseDaily
import com.example.expenseutility.model.ExpenseMonthly
import com.example.expenseutility.model.ExpensePredefine
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean
import com.malas.appsr.malasapp.Constant
import com.malas.appsr.malasapp.R
import com.malas.appsr.malasapp.Utils
import com.malas.appsr.malasapp.adapter.ExpenseDailyAdapter
import com.malas.appsr.malasapp.model.attendance_report
import com.malas.appsr.malasapp.serverconnection.BackgroundWork
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class ExpenseManagement : AppCompatActivity() {

    var cal: ExpenseManagement? = null

    private var preExpense: ExpensePredefine? = null
    private var dailyExpenseList: MutableList<ExpenseDaily>? = ArrayList()
    private var attendanceList: MutableList<attendance_report>? = ArrayList()
    private var expenseDailyAdapter: ExpenseDailyAdapter? = null
    private var expenseRecyclerview: RecyclerView? = null
    private var tm = arrayOf("Bus", "Train", "Flight", "Taxi", "Bike")

    var alertDialog: AlertDialog? = null
    var backDaily: ImageView? = null
    var date: TextView? = null
    var workType: AutoCompleteTextView? = null
    var townFrom: AutoCompleteTextView? = null
    var townTo: AutoCompleteTextView? = null
    var da: EditText? = null
    var travelMode: AutoCompleteTextView? = null
    var startReading: EditText? = null
    var endReading: EditText? = null
    var ta: EditText? = null
    var inOut: EditText? = null
    var other: EditText? = null
    var addEvent: Button? = null

    var WorkType_Input: TextInputLayout? = null
    var TownFrom_Input: TextInputLayout? = null
    var TownTo_Input: TextInputLayout? = null
    var Da_Input: TextInputLayout? = null
    var TravelMode_Input: TextInputLayout? = null
    var start_reading_Input: TextInputLayout? = null
    var end_reading_Input: TextInputLayout? = null
    var Ta_Input: TextInputLayout? = null
    var InOut_Input: TextInputLayout? = null
    var Other_Input: TextInputLayout? = null

    var travel_mode: ArrayAdapter<String>? = null
    var city: ArrayAdapter<String>? = null

    var start_img_string: String? = null
    var end_img_string: String? = null
    var ta_img_string: String? = null
    var other_img_string: String? = null
    var pass_img_string: String? = null
    var courier_img_string: String? = null
    var sample_img_string: String? = null

    var start_img: ImageButton? = null
    var end_img: ImageButton? = null
    var ta_img: ImageButton? = null
    var other_img: ImageButton? = null
    var pass_img: ImageButton? = null
    var courier_img: ImageButton? = null
    var sample_img: ImageButton? = null
    private var bitmap: Bitmap? = null

    var start_img_preview: ImageView? = null
    var end_img_preview: ImageView? = null
    var ta_img_preview: ImageView? = null
    var other_img_preview: ImageView? = null
    var pass_img_preview: ImageView? = null
    var courier_img_preview: ImageView? = null
    var sample_img_preview: ImageView? = null

    var start_img_preview_link: TextView? = null
    var end_img_preview_link: TextView? = null
    var ta_img_preview_link: TextView? = null
    var other_img_preview_link: TextView? = null
    var pass_img_preview_link: TextView? = null
    var courier_img_preview_link: TextView? = null
    var sample_img_preview_link: TextView? = null

    var complexPreferences: ComplexPreferences? = null
    var mUserLoginInfoBean: UserLoginInfoBean? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var expense = ExpenseMonthly()

    var dailyExpenseTotal: Int? = null

    val APP_TAG = "Malas_SR"
    var photoFileName = "photo.jpg"
    lateinit var photoFile: File

    val cityList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_expense_management)

        supportActionBar!!.title = "Expense Sheet"
        complexPreferences =
            ComplexPreferences.getComplexPreferences(this, Constant.UserRegInfoPref, MODE_PRIVATE)
        mUserLoginInfoBean =
            complexPreferences!!.getObject(Constant.UserRegInfoObj, UserLoginInfoBean::class.java)

        cal = findViewById(R.id.cal)

        expenseRecyclerview = findViewById(R.id.expense_summary)
        expenseRecyclerview!!.layoutManager = LinearLayoutManager(this)
        preExpense = ExpensePredefine()
        if (Utils.isInternetConnected(this)) {
            getPreExpense()
        }
        cal!!.gridView!!.setOnItemLongClickListener { parent, view, position, id ->
            when {
                view.findViewById<ImageView>(R.id.dayOfTheMonthCircleImage2).visibility == View.VISIBLE -> {
                    initalize(parent, position)
                    addEvent!!.text = "Update"
                    addEvent!!.setOnClickListener {
                        it.isEnabled = false
                        if (validate_data()) {
                            UpdateExpense(view, position)
                        }
                    }

                    dailyExpenseList!!.forEach {
                        if (it.date
                                .toString() == SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(cal!!.dates!![position])
                        ) {
                            setExpense(it)
                            cal!!.arrayadapter!!.filter.filter(null)
                            city!!.filter.filter(null)
                            travel_mode!!.filter.filter(null)

                        }
                    }
                }

                view.findViewById<ImageView>(R.id.dayOfTheMonthCircleImage1).visibility == View.VISIBLE -> {
                    initalize(parent, position)
                    addEvent!!.setOnClickListener {
                        it.isEnabled = false
                        if (validate_data()) {
                            SaveExpense(view, position)
                        }
                    }
                }

                view.findViewById<ImageView>(R.id.dayOfTheMonthCircleImage3).visibility == View.VISIBLE -> {
                    Snackbar.make(
                        view,
                        "Expense Already Submitted for " + SimpleDateFormat(
                            "MMMM yyyy",
                            Locale.getDefault()
                        ).format(cal!!.dates!![position])
                                + ".",
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(
                            Color.RED
                        ).show()

                }

                view.findViewById<ImageView>(R.id.dayOfTheMonthCircleImage4).visibility == View.VISIBLE -> {
                    Snackbar.make(
                        view,
                        "Expense Already Processed for " + SimpleDateFormat(
                            "MMMM yyyy",
                            Locale.getDefault()
                        ).format(cal!!.dates!![position])
                                + ".",
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(
                            Color.RED
                        ).show()

                }
                else -> {
                    Snackbar.make(
                        view,
                        /*"Attendance Not Marked for "*/"Expense Not Allowed For "+ SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(cal!!.dates!![position])
                                + ".",
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(
                            Color.RED
                        ).show()

                }
            }
            true
        }

        cal!!.next!!.setOnClickListener {
            cal!!.cal.add(Calendar.MONTH, 1)
            cal!!.setupCalendar()
            getPreExpense()

        }
        cal!!.previous!!.setOnClickListener {
            cal!!.cal.add(Calendar.MONTH, -1)
            cal!!.setupCalendar()
            getPreExpense()

        }


        cal!!.gridView!!.setOnItemClickListener { parent, view, position, id ->
            if (view.findViewById<ImageView>(R.id.dayOfTheMonthCircleImage1).visibility == View.VISIBLE) {
                initalize(parent, position)

                addEvent!!.setOnClickListener {
                    it.isEnabled = false
                    if (validate_data()) {
                        SaveExpense(view, position)
                    }
                }

            } else if (view.findViewById<ImageView>(
                    R.id.dayOfTheMonthCircleImage2
                ).visibility == View.VISIBLE
                || view.findViewById<ImageView>(
                    R.id.dayOfTheMonthCircleImage3
                ).visibility == View.VISIBLE
                || view.findViewById<ImageView>(
                    R.id.dayOfTheMonthCircleImage4
                ).visibility == View.VISIBLE
            ) {
                initalize(parent, position)

                dailyExpenseList!!.forEach {
                    if (it.date
                            .toString() == SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(cal!!.dates!![position])
                    ) {
                        setExpense(it)
                    }
                }
                workType!!.isEnabled = false
                townFrom!!.isEnabled = false
                townTo!!.isEnabled = false
                da!!.isEnabled = false
                travelMode!!.isEnabled = false
                startReading!!.isEnabled = false
                endReading!!.isEnabled = false
                ta!!.isEnabled = false
                inOut!!.isEnabled = false
                other!!.isEnabled = false
                addEvent!!.visibility = View.GONE
                other_img!!.visibility = View.INVISIBLE
                if (Ta_Input!!.visibility == View.VISIBLE) {
                    ta_img!!.visibility = View.INVISIBLE
                }
                if (start_reading_Input!!.visibility == View.VISIBLE) {
                    start_img!!.visibility = View.INVISIBLE
                }
                if (end_reading_Input!!.visibility == View.VISIBLE) {
                    end_img!!.visibility = View.INVISIBLE
                }
            } else {
                Snackbar.make(
                    view,
                    "Attendance Not Marked for " + SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(cal!!.dates!![position])
                            + ".",
                    Snackbar.LENGTH_LONG
                )
                    .setTextColor(
                        Color.RED
                    ).show()
            }
        }
    }

    private fun setExpense(it: ExpenseDaily) {
        setLayout(it.workType.toString())
        workType!!.setText(it.workType.toString())
        townFrom!!.setText(it.townFrom)
        townTo!!.setText(it.townTo)
        da!!.setText(it.da.toString())
        travelMode!!.setText(it.travelMode)
        startReading!!.setText(it.start.toString())
        endReading!!.setText(it.end.toString())
        if (travelMode!!.text.toString() == "Car") {

            start_reading_Input!!.visibility = View.VISIBLE
            end_reading_Input!!.visibility = View.VISIBLE
            ta_img!!.visibility = View.INVISIBLE
            start_img!!.visibility = View.VISIBLE
            end_img!!.visibility = View.VISIBLE
            ta!!.isEnabled = false

        } else {

            start_reading_Input!!.visibility = View.GONE
            end_reading_Input!!.visibility = View.GONE
            start_img!!.visibility = View.GONE
            end_img!!.visibility = View.GONE
        }
        ta!!.setText(it.ta.toString())
        inOut!!.setText(it.inOut.toString())
        other!!.setText(it.other.toString())
        other!!.requestFocus()
        if (it.start!! > 0) {
            try {
                start_img_preview_link!!.visibility = View.VISIBLE
                val url =
                    Constant.BASE_URL2 + "Expense_Attachments/Expense_Daily/" + it.prefix + "-" + it.empId + it.date + "start.jpg"

                Glide.with(this)
                    .load(url)
                    .into(start_img_preview as ImageView)
                val bitmapDrawable = start_img_preview!!.drawable as BitmapDrawable
                bitmap = bitmapDrawable.bitmap
                start_img_string = imageUploadToServerFunction()
                start_img!!.setImageResource(R.drawable.ic_twotone_check_24)
            } catch (e: Exception) {
                Log.e("Start_Img", e.message.toString())
                start_img_preview_link!!.visibility = View.GONE
                start_img_string = null
                start_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        }
        if (it.end!! > 0) {
            try {

                end_img_preview_link!!.visibility = View.VISIBLE
                val url =
                    Constant.BASE_URL2 + "Expense_Attachments/Expense_Daily/" + it.prefix + "-" + it.empId + it.date + "end.jpg"

                Glide.with(this)
                    .load(url)
                    .into(end_img_preview as ImageView)
                val bitmapDrawable = end_img_preview!!.drawable as BitmapDrawable
                bitmap = bitmapDrawable.bitmap
                end_img_string = imageUploadToServerFunction()
                end_img!!.setImageResource(R.drawable.ic_twotone_check_24)

            } catch (e: Exception) {

                Log.e("End_Img", e.message.toString())

                end_img_preview_link!!.visibility = View.GONE
                end_img_string = null
                end_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        }
        if (it.ta!! > 0) {

            try {

                ta_img_preview_link!!.visibility = View.VISIBLE
                val url =
                    Constant.BASE_URL2 + "Expense_Attachments/Expense_Daily/" + it.prefix + "-" + it.empId + it.date + "ta.jpg"

                Glide.with(this)
                    .load(url)
                    .into(ta_img_preview as ImageView)
                val bitmapDrawable = ta_img_preview!!.drawable as BitmapDrawable
                bitmap = bitmapDrawable.bitmap
                ta_img_string = imageUploadToServerFunction()
                ta_img!!.setImageResource(R.drawable.ic_twotone_check_24)

            } catch (e: Exception) {
                Log.e("Ta_Img", e.message.toString())
                ta_img_preview_link!!.visibility = View.GONE
                ta_img_string = null
                ta_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        }
        if (it.other!! > 0) {
            other_img_preview_link!!.visibility = View.VISIBLE

            try {

                val url =
                    Constant.BASE_URL2 + "Expense_Attachments/Expense_Daily/" + it.prefix + "-" + it.empId + it.date + "other.jpg"

                Glide.with(this)
                    .load(url)
                    .into(other_img_preview as ImageView)


                val bitmapDrawable = other_img_preview!!.drawable as BitmapDrawable


                bitmap = bitmapDrawable.bitmap
                other_img_string = imageUploadToServerFunction()
                other_img!!.setImageResource(R.drawable.ic_twotone_check_24)

            } catch (e: Exception) {
                Log.e("Other_Img", e.message.toString())
                other_img_preview_link!!.visibility = View.GONE
                other_img_string = null
                other_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        }
    }

    private fun setWorkType(parent: AdapterView<*>, position: Int) {
        setLayout(parent.getItemAtPosition(position).toString())
        townFrom!!.text.clear()
        townTo!!.text.clear()

        when (parent.getItemAtPosition(position).toString()) {
            "Head Quarter(HQ)" -> {
                da!!.setText(preExpense!!.daHq.toString())
                townFrom!!.setText(mUserLoginInfoBean!!.cname)
                townTo!!.setText(mUserLoginInfoBean!!.cname)
            }
            "Up Country(UC)" -> {
                da!!.setText(preExpense!!.daUc.toString())
            }
            "Night Halt(NH)" -> {
                da!!.setText(preExpense!!.daNh.toString())
            }
            "Team Meeting(TM)" -> {
                da!!.setText("0")

            }
            "Work From Home(WFH)" -> {
                da!!.setText("0")
                townFrom!!.setText(mUserLoginInfoBean!!.cname)
                townTo!!.setText(mUserLoginInfoBean!!.cname)
            }
        }
    }

    private fun setLayout(data: String) {
        HideAllFields()
        TownFrom_Input!!.visibility = View.VISIBLE
        TownTo_Input!!.visibility = View.VISIBLE
        Da_Input!!.visibility = View.VISIBLE
        Other_Input!!.visibility = View.VISIBLE
        other_img!!.visibility = View.VISIBLE

        if (data == "Head Quarter(HQ)" || data == "Work From Home(WFH)") {
            townFrom!!.isEnabled = false
            townTo!!.isEnabled = false
        } else {
            townFrom!!.isEnabled = true
            townTo!!.isEnabled = true
            TravelMode_Input!!.visibility = View.VISIBLE
            Ta_Input!!.visibility = View.VISIBLE
            ta_img!!.visibility = View.VISIBLE
            if (preExpense!!.inOut != 0) {
                InOut_Input!!.visibility = View.VISIBLE
            }
        }
    }

    private fun setTravelMode(parent: AdapterView<*>, view: View, position: Int) {
        when (parent.getItemAtPosition(position).toString()) {
            "Car" -> {

                ta!!.isEnabled = false
                start_reading_Input!!.visibility = View.VISIBLE
                end_reading_Input!!.visibility = View.VISIBLE
                InOut_Input!!.visibility = View.GONE
                ta_img!!.visibility = View.INVISIBLE
                start_img!!.visibility = View.VISIBLE
                end_img!!.visibility = View.VISIBLE
            }
            else -> {
                ta!!.isEnabled = true
                start_reading_Input!!.visibility = View.GONE
                end_reading_Input!!.visibility = View.GONE

                start_img!!.visibility = View.GONE
                end_img!!.visibility = View.GONE
                ta_img!!.visibility = View.VISIBLE

                if (preExpense!!.inOut != 0) {
                    InOut_Input!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun SaveExpense(view: View, position: Int) {
        val back = BackgroundWork(this)
        back.execute(
            "Daily Expense",
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal!!.dates!![position]),
            workType!!.text.toString(),
            townFrom!!.text.toString().trim().uppercase(Locale.getDefault()),
            townTo!!.text.toString().trim().uppercase(Locale.getDefault()),
            travelMode!!.text.toString(),
            startReading!!.text.toString(),
            endReading!!.text.toString(),
            ta!!.text.toString(),
            inOut!!.text.toString(),
            other!!.text.toString(),
            start_img_string.toString(),
            end_img_string.toString(),
            ta_img_string.toString(),
            other_img_string.toString()
        )

        back.dailog!!.setOnDismissListener {
            addEvent!!.isEnabled = true
            when (back.result) {
                "Expense Added Successfully." -> {
                    Snackbar.make(
                        view,
                        "Expense Added Successfully for " + SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(
                            cal!!.dates!![position]
                        )
                                + ".",
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(
                            Color.GREEN
                        ).show()
                    getDailyExpense()
                }

                "Clock-In not Approved" -> {
                    Snackbar.make(
                        view,
                        "You have not CLOCKED OUT yet OR  CLOCKIN request is Pending at Manager ",
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(
                            Color.WHITE
                        ).show()

                }
                else -> {
                    Snackbar.make(view, "Something Went Wrong.", Snackbar.LENGTH_LONG)
                        .setTextColor(
                            Color.RED
                        ).show()

                }
            }
            cal!!.setupCalendar()
            alertDialog!!.dismiss()

        }
    }

    private fun UpdateExpense(view: View, position: Int) {
        val back = BackgroundWork(this)
        back.execute(
            "Update Daily Expense",
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal!!.dates!![position]),
            workType!!.text.toString(),
            townFrom!!.text.toString().uppercase(Locale.getDefault()).trim(),
            townTo!!.text.toString().uppercase(Locale.getDefault()).trim(),
            travelMode!!.text.toString(),
            startReading!!.text.toString(),
            endReading!!.text.toString(),
            ta!!.text.toString(),
            inOut!!.text.toString(),
            other!!.text.toString(),
            start_img_string.toString(),
            end_img_string.toString(),
            ta_img_string.toString(),
            other_img_string.toString()
        )

        back.dailog!!.setOnDismissListener {
            addEvent!!.isEnabled = true
            if (back.result == "Expense Updated Successfully.") {
                Snackbar.make(
                    view,
                    "Expense Updated Successfully for " + SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(
                        cal!!.dates!![position]
                    )
                            + ".",
                    Snackbar.LENGTH_LONG
                )
                    .setTextColor(
                        Color.GREEN
                    ).show()
                getDailyExpense()
            } else {
                Snackbar.make(view, "Data Not Changed", Snackbar.LENGTH_LONG)
                    .setTextColor(
                        Color.RED
                    ).show()
            }
            cal!!.setupCalendar()
            alertDialog!!.dismiss()
        }
    }

    fun initalize(parent: AdapterView<*>, position: Int) {
        val builder = AlertDialog.Builder(this)

        builder.setCancelable(true)

        val addview =
            LayoutInflater.from(parent.context)!!.inflate(R.layout.add_expense_layout, null)
        backDaily = addview.findViewById(R.id.back_daily)
        date = addview.findViewById(R.id.selected_date)
        workType = addview.findViewById(R.id.work_type)
        townFrom = addview.findViewById(R.id.town_from)
        townTo = addview.findViewById(R.id.town_to)
        da = addview.findViewById(R.id.da)
        travelMode = addview.findViewById(R.id.travel_mode)
        startReading = addview.findViewById(R.id.start_reading)
        endReading = addview.findViewById(R.id.end_reading)
        ta = addview.findViewById(R.id.ta)
        inOut = addview.findViewById(R.id.inout)
        other = addview.findViewById(R.id.other)
        addEvent = addview.findViewById(R.id.addEvent)

        WorkType_Input = addview.findViewById(R.id.input_work_type)
        TownFrom_Input = addview.findViewById(R.id.input_town_from)
        TownTo_Input = addview.findViewById(R.id.input_town_to)
        Da_Input = addview.findViewById(R.id.input_da)
        TravelMode_Input = addview.findViewById(R.id.input_travel_mode)
        start_reading_Input = addview.findViewById(R.id.input_start_reading)
        end_reading_Input = addview.findViewById(R.id.input_end_reading)
        Ta_Input = addview.findViewById(R.id.input_ta)
        InOut_Input = addview.findViewById(R.id.input_inout)
        Other_Input = addview.findViewById(R.id.input_other)

        start_img = addview.findViewById(R.id.start_img)
        end_img = addview.findViewById(R.id.end_img)
        ta_img = addview.findViewById(R.id.ta_img)
        other_img = addview.findViewById(R.id.other_img)

        start_img_preview = addview.findViewById(R.id.start_img_preview)
        end_img_preview = addview.findViewById(R.id.end_img_preview)
        ta_img_preview = addview.findViewById(R.id.ta_img_preview)
        other_img_preview = addview.findViewById(R.id.other_img_preview)

        start_img_preview_link = addview.findViewById(R.id.start_img_preview_link)
        end_img_preview_link = addview.findViewById(R.id.end_img_preview_link)
        ta_img_preview_link = addview.findViewById(R.id.ta_img_preview_link)
        other_img_preview_link = addview.findViewById(R.id.other_img_preview_link)

        start_img_string = null
        end_img_string = null
        ta_img_string = null
        other_img_string = null

        //back button
        backDaily!!.setOnClickListener {
            alertDialog!!.dismiss()
        }

        //selected date
        date!!.text =
            SimpleDateFormat(
                "EEE, d MMM yyyy",
                Locale.getDefault()
            ).format(cal!!.dates!![position])
        date!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        //disable focus on Travel Mode
        travelMode!!.isFocusable = false
        travelMode!!.isCursorVisible = false

        //disable focus on Work Type
        workType!!.isFocusable = false
        workType!!.isCursorVisible = false

        cal!!.arrayadapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, cal!!.wt)

        travel_mode =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, tm)

        workType!!.setAdapter(cal!!.arrayadapter)

        townFrom!!.setAdapter(city)

        townTo!!.setAdapter(city)

        travelMode!!.setAdapter(travel_mode)


        workType!!.setOnClickListener {
            workType!!.showDropDown()
        }

        townFrom!!.setOnClickListener {
            townFrom!!.showDropDown()
        }
        townFrom!!.setOnItemClickListener { _, _, _, _ ->
            TownFrom_Input!!.isErrorEnabled = false
            townTo!!.requestFocus()
        }

        townFrom!!.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {

                if (!cityList.any {
                        it.uppercase(Locale.getDefault()) == townFrom!!.text.toString().uppercase(
                            Locale.getDefault()
                        )
                    }) {
                    //Town From validation

                    TownFrom_Input!!.isErrorEnabled = true
                    townFrom!!.error = "Invalid City"
                } else {
                    TownFrom_Input!!.isErrorEnabled = false
                }
            }
        }

        townTo!!.setOnClickListener {

            townTo!!.showDropDown()

        }
        townTo!!.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Log.d("TO City", cityList.any {
                    it.uppercase(Locale.getDefault()) == townTo!!.text.toString().uppercase(
                        Locale.getDefault()
                    )
                }.toString())

                Log.d("TO City", cityList.toString())
                if (!cityList.any {
                        it.uppercase(Locale.getDefault()).trim() == townTo!!.text.toString().trim()
                            .uppercase(
                                Locale.getDefault()
                            )
                    }) {
                    //Town From validation

                    TownTo_Input!!.isErrorEnabled = true
                    townTo!!.error = "Invalid City"

                } else {
                    TownTo_Input!!.isErrorEnabled = false

                }
            }
        }
        travelMode!!.setOnClickListener {
            travelMode!!.showDropDown()
        }

        workType!!.setOnItemClickListener { parents, view, positions, id ->
            setWorkType(parents, positions)
        }

        travelMode!!.setOnItemClickListener { parents, view, positions, id ->

            ta!!.setText("")
            startReading!!.setText("")
            endReading!!.setText("")
            ta_img_string = null
            start_img_string = null
            end_img_string = null
            ta_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            start_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            end_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            setTravelMode(parents, view, positions)

        }

        startReading!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (travelMode!!.text.toString() == "Car") {
                    ta!!.setText(calculateDistance().toString())

                }
            }

            override fun afterTextChanged(s: Editable?) {
                start_img_string = null
                start_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }

        })

        endReading!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (travelMode!!.text.toString() == "Car") {
                    ta!!.setText(calculateDistance().toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                end_img_string = null
                end_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        })
        ta!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                ta_img_string = null
                ta_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)

            }

        })

        other!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                Log.d("Other Before", other!!.text.toString())

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                other_img_string = null
                other_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)

            }

        })

        start_img!!.setOnClickListener {
            if (startReading!!.text.toString()
                    .isEmpty() || startReading!!.text.toString() == "0"
            ) {
                start_reading_Input!!.isErrorEnabled = true
                startReading!!.error = "Reading value Can't Be Empty or '0' "
            } else {
                start_reading_Input!!.isErrorEnabled = false
                showImageDailog(0, 1)
            }
        }
        end_img!!.setOnClickListener {
            if (endReading!!.text.toString()
                    .isEmpty() || endReading!!.text.toString() == "0"
            ) {
                end_reading_Input!!.isErrorEnabled = true
                endReading!!.error = "Reading value Can't Be Empty or '0' "
            } else {
                end_reading_Input!!.isErrorEnabled = false
                showImageDailog(2, 3)
            }
        }
        ta_img!!.setOnClickListener {
            if (ta!!.text.toString()
                    .isEmpty() || ta!!.text.toString() == "0"
            ) {
                Ta_Input!!.isErrorEnabled = true
                ta!!.error = "Allowance Can't Be Empty or '0' "
            } else {
                Ta_Input!!.isErrorEnabled = false
                showImageDailog(4, 5)
            }
        }
        other_img!!.setOnClickListener {
            if (other!!.text.toString()
                    .isEmpty() || other!!.text.toString() == "0"
            ) {
                Other_Input!!.isErrorEnabled = true
                other!!.error = "Allowance Can't Be Empty or '0' "
            } else {
                Other_Input!!.isErrorEnabled = false
                showImageDailog(6, 7)
            }
        }

        start_img_preview_link!!.setOnClickListener {
            img_preview(start_img_string)
        }
        end_img_preview_link!!.setOnClickListener {
            img_preview(end_img_string)
        }
        ta_img_preview_link!!.setOnClickListener {
            img_preview(ta_img_string)
        }
        other_img_preview_link!!.setOnClickListener {
            img_preview(other_img_string)
        }

        HideAllFields()

        builder.setView(addview)
        alertDialog = builder.create()
        alertDialog!!.show()
        alertDialog!!.setOnCancelListener {
            cal!!.setupCalendar()
        }
    }

    private fun img_preview(encodedString: String?) {

        try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: java.lang.Exception) {
            e.message
        }


        val alt = android.app.AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.image_preview, null)
        val img = dv.findViewById<ImageView>(R.id.image_preview)
        img.setImageBitmap(bitmap)
        //    val cam = dv.findViewById<Button>(R.id.dailog_camera)
        //    val gallery = dv.findViewById<Button>(R.id.dailog_gallery)
        alt.setView(dv)

        img.setOnClickListener {
            val byteArrayOutputStreamObject = ByteArrayOutputStream()
            // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject)

            val i = Intent(this, ImageZoomPreview::class.java)
            i.putExtra("IMAGE_BYTES", byteArrayOutputStreamObject.toByteArray())
            startActivity(i)
        }

        val dialog = alt.create()
        dialog.show()

    }

    private fun showImageDailog(camera: Int, file: Int) {

        val alt = android.app.AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.camera_dailog, null)
        val cam = dv.findViewById<Button>(R.id.dailog_camera)
        val gallery = dv.findViewById<Button>(R.id.dailog_gallery)
        alt.setView(dv)
        val dialog = alt.create()
        dialog.show()

        cam.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Create a File reference for future access
            photoFile = getPhotoFileUri(photoFileName)

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.malas.appsr.fileprovider", photoFile)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // Start the image capture intent to take photo
            startActivityForResult(intent, camera)

            //   startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), camera)
            dialog.dismiss()
        }

        gallery.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ), file
            )
            dialog.dismiss()
        }
    }

    @Throws(IOException::class)
    private fun getPhotoFileUri(photoFileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.

        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename

        return File(mediaStorageDir.path + File.separator + photoFileName)

    }

    fun initalize_monthly() {
        var alertDialog: AlertDialog? = null

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val addview = layoutInflater.inflate(R.layout.add_monthly_expense_layout, null)
        val back_monthly: ImageView = addview.findViewById(R.id.back_monthly)
        val date: TextView = addview.findViewById(R.id.month_year_monthly_expense)
        val mobile: EditText = addview.findViewById(R.id.mobile_monthly_expense)
        val travel_pass: EditText = addview.findViewById(R.id.travel_pass_monthly_expense)
        val courier_stationery: EditText =
            addview.findViewById(R.id.courier_stationery_monthly_expense)
        val sample: EditText = addview.findViewById(R.id.sample_monthly_expense)
        val save: Button = addview.findViewById(R.id.monthly_save)
        val process: Button = addview.findViewById(R.id.monthly_Process)

        val travel_pass_Input: TextInputLayout =
            addview.findViewById(R.id.input_travel_pass_monthly_expense)
        val courier_stationery_Input: TextInputLayout =
            addview.findViewById(R.id.input_courier_stationery_monthly_expense)
        val sample_Input: TextInputLayout =
            addview.findViewById(R.id.input_sample_monthly_expense)

        pass_img = addview.findViewById(R.id.travel_pass_img)
        courier_img = addview.findViewById(R.id.courier_img)
        sample_img = addview.findViewById(R.id.sample_img)

        pass_img_preview = addview.findViewById(R.id.travel_pass_img_preview)
        courier_img_preview = addview.findViewById(R.id.courier_img_preview)
        sample_img_preview = addview.findViewById(R.id.sample_img_preview)

        pass_img_preview_link = addview.findViewById(R.id.travel_pass_img_preview_link)
        courier_img_preview_link = addview.findViewById(R.id.courier_img_preview_link)
        sample_img_preview_link = addview.findViewById(R.id.sample_img_preview_link)

        //Back Monthly
        back_monthly.setOnClickListener {
            alertDialog!!.dismiss()
        }

        //selected date
        date.text =
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal!!.cal.time)

        date.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

        travel_pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                pass_img_string = null
                pass_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)

            }

        })
        courier_stationery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                courier_img_string = null
                courier_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        })
        sample.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                sample_img_string = null
                sample_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
            }
        })


        if (expense.pass.toString() != "null" && expense.pass.toString()
                .isNotEmpty()
        ) {
            travel_pass.setText(expense.pass.toString())
            save.text = "Update"
            if (expense.pass!! > 0) {
                try {
                    pass_img_preview_link!!.visibility = View.VISIBLE
                    val url =
                        Constant.BASE_URL2 + "Expense_Attachments/Expense_Monthly/" + expense.prefix + "-" + expense.empId + expense.monthYear + "pass.jpg"
                    Glide.with(this)
                        .load(url)
                        .into(pass_img_preview as ImageView)
                    val bitmapDrawable = pass_img_preview!!.drawable as BitmapDrawable
                    bitmap = bitmapDrawable.bitmap
                    pass_img_string = imageUploadToServerFunction()
                    pass_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                } catch (e: Exception) {
                    Log.e("End_Img", e.message.toString())

                    pass_img_preview_link!!.visibility = View.GONE
                    pass_img_string = null
                    pass_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)

                }
            }
        }

        if (expense.courier.toString() != "null" && expense.courier.toString()
                .isNotEmpty()
        ) {
            courier_stationery.setText(expense.courier.toString())
            save.text = "Update"
            if (expense.courier!! > 0) {
                try {

                    courier_img_preview_link!!.visibility = View.VISIBLE
                    val url =
                        Constant.BASE_URL2 + "Expense_Attachments/Expense_Monthly/" + expense.prefix + "-" + expense.empId + expense.monthYear + "courier.jpg"

                    Glide.with(this)
                        .load(url)
                        .into(courier_img_preview as ImageView)
                    val bitmapDrawable = courier_img_preview!!.drawable as BitmapDrawable
                    bitmap = bitmapDrawable.bitmap
                    courier_img_string = imageUploadToServerFunction()
                    courier_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                } catch (e: Exception) {
                    Log.e("End_Img", e.message.toString())
                    courier_img_preview_link!!.visibility = View.GONE
                    courier_img_string = null
                    courier_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
                }
            }
        }

        if (expense.sample.toString() != "null" && expense.sample.toString()
                .isNotEmpty()
        ) {
            sample.setText(expense.sample.toString())
            save.text = "Update"

            if (expense.sample!! > 0) {
                try {

                    sample_img_preview_link!!.visibility = View.VISIBLE
                    val url =
                        Constant.BASE_URL2 + "Expense_Attachments/Expense_Monthly/" + expense.prefix + "-" + expense.empId + expense.monthYear + "sample.jpg"

                    Glide.with(this)
                        .load(url)
                        .into(sample_img_preview as ImageView)
                    val bitmapDrawable = sample_img_preview!!.drawable as BitmapDrawable
                    bitmap = bitmapDrawable.bitmap
                    sample_img_string = imageUploadToServerFunction()
                    sample_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                } catch (e: Exception) {
                    Log.e("End_Img", e.message.toString())
                    sample_img_preview_link!!.visibility = View.GONE
                    sample_img_string = null
                    sample_img!!.setImageResource(R.drawable.ic_baseline_attach_file_24)
                }
            }
        }

        if (save.text == "Save") {
            process.visibility = View.GONE
        }
        val now = Calendar.getInstance()
        if (now.get(Calendar.MONTH) != cal!!.cal.get(Calendar.MONTH) || (expense.status != "Pending" && expense.status != null)) {

            now.add(Calendar.DATE, -7)

            if (now.get(Calendar.MONTH) != cal!!.cal.get(Calendar.MONTH) || (now.get(Calendar.MONTH) == cal!!.cal.get(
                    Calendar.MONTH
                ) && (expense.status != "Pending" && expense.status != null))
            ) {
                /*   Toast.makeText(
                           applicationContext,
                           "Expense Submit time for this month is Already Expired Please Contact Your Manager.",
                           Toast.LENGTH_LONG
                   ).show()
                  */
                addview.findViewById<TextView?>(R.id.time_expire).apply {
                    text = String.format(
                        "* Expense Already Submitted. OR You Have Exceeded Deadline For Expense Submission. Last Date For Submission Was 07-${
                            cal!!.cal.get(Calendar.MONTH) + 2
                        }-${cal!!.cal.get(Calendar.YEAR)}"
                    )

                    visibility = View.VISIBLE
                }

                save.visibility = View.GONE
                process.text = "Preview"
            }
        }
        pass_img!!.setOnClickListener {
            if (travel_pass.text.toString()
                    .isEmpty() || travel_pass.text.toString() == "0"
            ) {
                travel_pass_Input.isErrorEnabled = true
                travel_pass.error = "Allowance Can't Be Empty or '0' "
            } else {
                travel_pass_Input.isErrorEnabled = false

                showImageDailog(8, 9)
            }
        }
        courier_img!!.setOnClickListener {
            if (courier_stationery.text.toString()
                    .isEmpty() || courier_stationery.text.toString() == "0"
            ) {
                courier_stationery_Input.isErrorEnabled = true
                courier_stationery.error = "Allowance Can't Be Empty or '0' "
            } else {
                courier_stationery_Input.isErrorEnabled = false

                showImageDailog(10, 11)
            }
        }
        sample_img!!.setOnClickListener {
            if (sample.text.toString()
                    .isEmpty() || sample.text.toString() == "0"
            ) {
                sample_Input.isErrorEnabled = true
                sample.error = "Allowance Can't Be Empty or '0' "
            } else {
                sample_Input.isErrorEnabled = false
                showImageDailog(12, 13)
            }
        }

        mobile.setText(preExpense!!.mobile.toString())

        pass_img_preview_link!!.setOnClickListener {
            img_preview(pass_img_string)
        }
        courier_img_preview_link!!.setOnClickListener {
            img_preview(courier_img_string)
        }
        sample_img_preview_link!!.setOnClickListener {
            img_preview(sample_img_string)
        }
        save.setOnClickListener {
            it.isEnabled = false
            var flag = true
            travel_pass_Input.isErrorEnabled = false
            courier_stationery_Input.isErrorEnabled = false
            sample_Input.isErrorEnabled = false

            if (travel_pass.text.toString().isNotEmpty() && courier_stationery.text.toString()
                    .isNotEmpty() && sample.text.toString().isNotEmpty()
            ) {

                if (travel_pass.text.toString().isNotEmpty() && travel_pass.text.toString()
                        .toInt() != 0 && pass_img_string == null
                ) {
                    travel_pass_Input.isErrorEnabled = true
                    travel_pass.error = "Please Provide Supporting Attachment's"
                    flag = false
                }
                if (courier_stationery.text.toString()
                        .isNotEmpty() && courier_stationery.text.toString()
                        .toInt() != 0 && courier_img_string == null
                ) {
                    courier_stationery_Input.isErrorEnabled = true
                    courier_stationery.error = "Please Provide Supporting Attachment's"
                    flag = false
                }
                if (sample.text.toString().isNotEmpty() && sample.text.toString()
                        .toInt() != 0 && sample_img_string == null
                ) {
                    sample_Input.isErrorEnabled = true
                    sample.error = "Please Provide Supporting Attachment's"
                    flag = false
                }
            } else {
                if (travel_pass.text.toString().isEmpty()) {
                    travel_pass_Input.isErrorEnabled = true
                    travel_pass.error = "Enter the Amount.(If Not Applicable Enter 0)"
                    flag = false
                }
                if (courier_stationery.text.toString().isEmpty()) {
                    courier_stationery_Input.isErrorEnabled = true
                    courier_stationery.error = "Enter the Amount.(If Not Applicable Enter 0)"
                    flag = false
                }
                if (sample.text.toString().isEmpty()) {
                    sample_Input.isErrorEnabled = true
                    sample.error = "Enter the Amount.(If Not Applicable Enter 0)"
                    flag = false
                }
            }
            if (save.text == "Save" && flag) {
                val back = BackgroundWork(this)
                back.execute(
                    "Monthly Expense",
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal!!.cal.time)
                        .toString(),
                    travel_pass.text.toString(),
                    courier_stationery.text.toString(),
                    sample.text.toString(),
                    pass_img_string.toString(),
                    courier_img_string.toString(),
                    sample_img_string.toString(),
                    "Pending",
                    "Save"
                )

                back.dailog!!.setOnDismissListener {
                    save.isEnabled = true
                    if (back.result == "Expense Added Successfully.") {
                        Snackbar.make(
                            findViewById(R.id.expense_summary),
                            "Monthly Expense Submitted ",
                            Snackbar.LENGTH_LONG
                        )
                            .setTextColor(
                                Color.GREEN
                            ).show()
                        getMonthlyExpense()
                        process.visibility = View.VISIBLE
                    } else {
                        Snackbar.make(
                            findViewById(R.id.expense_summary),
                            "Something Went Wrong.",
                            Snackbar.LENGTH_LONG
                        )
                            .setTextColor(
                                Color.RED
                            ).show()
                    }
                    cal!!.setupCalendar()
                    alertDialog!!.dismiss()
                }
            } else if (save.text == "Update" && flag) {
                val back = BackgroundWork(this)
                back.execute(
                    "Monthly Expense",
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal!!.cal.time)
                        .toString(),
                    travel_pass.text.toString(),
                    courier_stationery.text.toString(),
                    sample.text.toString(),
                    pass_img_string.toString(),
                    courier_img_string.toString(),
                    sample_img_string.toString(),
                    "Pending",
                    "Update"
                )

                back.dailog!!.setOnDismissListener {
                    save.isEnabled = true
                    if (back.result == "Expense Updated Successfully.") {

                        Snackbar.make(
                            findViewById(R.id.expense_summary),
                            "Monthly Updated Submitted ",
                            Snackbar.LENGTH_LONG
                        )
                            .setTextColor(
                                Color.GREEN
                            ).show()
                        getMonthlyExpense()
                    } else {
                        Snackbar.make(
                            findViewById(R.id.expense_summary),
                            "Something Went Wrong.",
                            Snackbar.LENGTH_LONG
                        )
                            .setTextColor(
                                Color.RED
                            ).show()

                    }
                    cal!!.setupCalendar()
                    alertDialog!!.dismiss()
                }
            }
            if (!flag) {
                save.isEnabled = true
            }
        }
        process.setOnClickListener {
            if (process.text == "Submit") {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Expense Managment")
                    .setCancelable(true)

                if (attendanceList!!.size > dailyExpenseList!!.size && dailyExpenseList!!.size > 0) {
                    var msg = "Please Mark Expense For Following Dates:\n"
                    var i = 0
                    attendanceList!!.forEach {

                        if (dateFormat.format(dateFormat.parse(it.gettimein() as String) as Date) != dateFormat.format(
                                dateFormat.parse(dailyExpenseList!![i].date as String) as Date
                            )
                        ) {
                            msg += SimpleDateFormat("dd-MM-yyyy, EEEE", Locale.getDefault()).format(
                                dateFormat.parse(
                                    it.gettimein() as String
                                )
                                        as Date
                            ) + "\n"
                            i--
                        }
                        if (i < dailyExpenseList!!.size - 1) {
                            i++
                        }
                    }
                    alert.setMessage(msg)
                } else {
                    alert.setMessage("Do You Want to Process Your Expense?")
                        .setPositiveButton(
                            "yes"
                        ) { dialog: DialogInterface, i: Int ->
                            alertDialog!!.dismiss()
                            preview()
                        }
                        .setNegativeButton(
                            "No"
                        ) { dialog: DialogInterface, i: Int ->

                        }
                }
                alert.show()
            } else if (process.text == "Preview") {
                preview()
            }
        }

        builder.setView(addview)
        alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setOnCancelListener {
            cal!!.setupCalendar()
        }
    }

    private fun preview() {

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        val addview = layoutInflater.inflate(R.layout.expense_preview, null)

        val name: TextView = addview.findViewById(R.id.expense_name)
        val month: TextView = addview.findViewById(R.id.expense_month)
        val sub_total: TextView = addview.findViewById(R.id.expense_sub)
        val mobile: TextView = addview.findViewById(R.id.expense_mobile)
        val rail_pass: TextView = addview.findViewById(R.id.expense_pass)
        val courier: TextView = addview.findViewById(R.id.expense_courier)
        val sample: TextView = addview.findViewById(R.id.expense_sample)
        val total: TextView = addview.findViewById(R.id.expense_total)
        val submit: Button = addview.findViewById(R.id.expense_submit)
        val cancel: TextView = addview.findViewById(R.id.expense_cancel)

        name.text = mUserLoginInfoBean!!.userName
        month.text = cal!!.current_month!!.text
        sub_total.text = dailyExpenseTotal.toString()
        mobile.text = preExpense!!.mobile.toString()
        rail_pass.text = expense.pass.toString()
        courier.text = expense.courier.toString()
        sample.text = expense.sample.toString()
        total.text = String.format(
            "${
                dailyExpenseTotal!! + preExpense!!.mobile!! + expense.pass!!
                        + expense.courier!! + expense.sample!!
            }"
        )
        val recyclerviewPreview: RecyclerView = addview.findViewById(R.id.expense_preview_view)
        recyclerviewPreview.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        recyclerviewPreview.addItemDecoration(dividerItemDecoration)
        recyclerviewPreview.adapter = ExpensePreviewtAdapter(dailyExpenseList)

        val now = Calendar.getInstance()
        if (now.get(Calendar.MONTH) != cal!!.cal.get(Calendar.MONTH) || expense.status != "Pending") {

            now.add(Calendar.DATE, -7)
            if (now.get(Calendar.MONTH) != cal!!.cal.get(Calendar.MONTH) || expense.status != "Pending") {
                /*    Toast.makeText(
                            applicationContext,
                            "Expense Submit time for this month is Already Expired Please Contact Your Manager.",
                            Toast.LENGTH_LONG
                    ).show()*/
                submit.visibility = View.GONE
            }
        }
        submit.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Expense Management")
                .setCancelable(true)
            alert.setMessage("This is  final Submission. You can not make any changes once it is submitted.\n\nDo You Want to Submit Your monthly Expense?")
                .setPositiveButton(
                    "yes"
                ) { _: DialogInterface, _: Int ->
                    alertDialog!!.dismiss()
                    val back = BackgroundWork(this)
                    back.execute(
                        "Submit Expense",
                        String.format("%02d", cal!!.cal.get(Calendar.MONTH) + 1),
                        cal!!.cal.get(Calendar.YEAR).toString()
                    )
                    back.dailog!!.setOnDismissListener {
                        val alert = AlertDialog.Builder(this)
                        alert.setTitle("Expense Management")
                            .setCancelable(true)
                        if (back.result == "Expense Submitted Successfully.") {

                            alert.setMessage("Your monthly sales expense is submitted successfully, please ask your manager to approve it.")
                                .setPositiveButton(
                                    "yes"
                                ) { _: DialogInterface, _: Int ->
                                    alertDialog!!.dismiss()
                                    getDailyExpense()
                                    cal!!.setupCalendar()
                                }
                                .show()
                        } else {
                            alert.setTitle("Error!!!")
                            alert.setMessage("\"TECHNICAL ERROR..!!!.\nDue to some technical error your monthly expense is not submitted. Please Try again after some time.")
                                .setPositiveButton(
                                    "yes"
                                ) { _: DialogInterface, i: Int ->
                                    alertDialog!!.dismiss()
                                }
                                .show()
                        }
                        alert.setOnDismissListener {
                            cal!!.setupCalendar()
                        }

                    }

                }
                .setNegativeButton(
                    "No"
                ) { _: DialogInterface, _: Int ->

                }

                .show()
        }

        cancel.setOnClickListener {
            alertDialog!!.dismiss()
        }

        builder.setView(addview)
        alertDialog = builder.create()
        alertDialog!!.show()
        alertDialog!!.setOnDismissListener {
            cal!!.setupCalendar()
        }
    }

    private fun validate_data(): Boolean {
        var flag = true
        //Work Type validation
        if (workType!!.text.toString().isEmpty()) {
            WorkType_Input!!.isErrorEnabled = true
            workType!!.error = "Select Work Type"
            flag = false
        } else {
            WorkType_Input!!.isErrorEnabled = false
            if (workType!!.text.toString() == "Head Quarter(HQ)"
                || workType!!.text.toString() == "Work From Home(WFH)"
                || (workType!!.text.toString() == "Team Meeting(TM)" && travelMode!!.text.isEmpty())
            ) {
                startReading!!.setText("0")
                endReading!!.setText("0")
                ta!!.setText("0")
                inOut!!.setText("0")
            }
        }

        //Town From validation
        if (townFrom!!.text.toString().isEmpty()) {

            TownFrom_Input!!.isErrorEnabled = true
            townFrom!!.error = "Select City"
            flag = false

        }  else {

            TownFrom_Input!!.isErrorEnabled = false

        }

        //Town-To validation
        if (townTo!!.text.toString().isEmpty()) {
            TownTo_Input!!.isErrorEnabled = true
            townTo!!.error = "Select City"
            flag = false

        }  else {
            TownTo_Input!!.isErrorEnabled = false
        }

        //Travel Mode validation
        if ((travelMode!!.text.toString().isEmpty()
                    && workType!!.text.toString() == "Up Country(UC)") || ((workType!!.text.toString() == "Team Meeting(TM)" ||
                    workType!!.text.toString() == "Night Halt(NH)")
                    && (ta!!.text.toString()
                .isNotEmpty() && ta!!.text.toString() != "0" && travelMode!!.text.toString()
                .isEmpty())
                    )
        ) {
            TravelMode_Input!!.isErrorEnabled = true
            travelMode!!.error = "Select Mode Of Travel"
            flag = false
        } else {
            TravelMode_Input!!.isErrorEnabled = false
        }

        //Start Speedometer Reading validation
        if (startReading!!.text.toString()
                .isEmpty() && (travelMode!!.text.toString() != "Car")
        ) {
            startReading!!.setText("0")
        } else if (startReading!!.text.toString()
                .isEmpty() && (travelMode!!.text.toString() == "Car")
        ) {
            start_reading_Input!!.isErrorEnabled = true
            startReading!!.error = "Enter Reading On Speedometer before Journey"
            flag = false
        } else if (startReading!!.text.toString() != "0" && start_img_string == null) {
            start_reading_Input!!.isErrorEnabled = true
            startReading!!.error = "Please Provide Supporting Attachment's"
            flag = false
        } else {
            start_reading_Input!!.isErrorEnabled = false
        }

        //Start Speedo-meter Reading validation
        if (endReading!!.text.toString()
                .isEmpty() && (travelMode!!.text.toString() != "Car")
        ) {
            endReading!!.setText("0")
        } else if (endReading!!.text.toString()
                .isEmpty() && (travelMode!!.text.toString() == "Car")
        ) {
            end_reading_Input!!.isErrorEnabled = true
            endReading!!.error = "Enter Reading On Speedometer After Journey"
            flag = false
        } else if (endReading!!.text.toString() != "0" && end_img_string == null) {
            end_reading_Input!!.isErrorEnabled = true
            endReading!!.error = "Please Provide Supporting Attachment's"
            flag = false
        } else {
            end_reading_Input!!.isErrorEnabled = false
        }

        //Travel Allowance validation
        if (
            (ta!!.text.toString().isEmpty()
                    && workType!!.text.toString() == "Up Country(UC)")
            || ((workType!!.text.toString() == "Team Meeting(TM)" || workType!!.text.toString() == "Night Halt(NH)")
                    && travelMode!!.text.toString().isNotEmpty() && ta!!.text.toString().isEmpty()
                    )
        ) {
            Ta_Input!!.isErrorEnabled = true
            ta!!.error = "Enter Traveling Expense Amount"
            flag = false
        } else if (ta!!.text.toString() != "0" && ta_img_string == null && travelMode!!.text.toString() != "Car") {
            Ta_Input!!.isErrorEnabled = true
            ta!!.error = "Please Provide Supporting Attachment's"
            flag = false
        } else {
            Ta_Input!!.isErrorEnabled = false
        }

        //In-Out Allowance validation
        if (inOut!!.text.toString().isEmpty()) {
            inOut!!.setText("0")
        } else if (preExpense!!.inOut!! < inOut!!.text.toString().toInt()) {
            InOut_Input!!.isErrorEnabled = true
            inOut!!.error = "Max In-Out Allowed is Rs." + preExpense!!.inOut
            flag = false
        }

        //Other Allowance validation
        if (other!!.text.toString().isEmpty()) {
            other!!.setText("0")
        } else if (other!!.text.toString() != "0" && other_img_string == null) {
            Other_Input!!.isErrorEnabled = true
            other!!.error = "Please Provide Supporting Attachment's"
            flag = false
        } else {
            Other_Input!!.isErrorEnabled = false
        }
        if (!flag) {
            addEvent!!.isEnabled = true
        }
        return flag
    }

    private fun HideAllFields() {
        TownFrom_Input!!.visibility = View.GONE
        TownTo_Input!!.visibility = View.GONE
        Da_Input!!.visibility = View.GONE
        TravelMode_Input!!.visibility = View.GONE
        start_reading_Input!!.visibility = View.GONE
        end_reading_Input!!.visibility = View.GONE
        Ta_Input!!.visibility = View.GONE
        InOut_Input!!.visibility = View.GONE
        Other_Input!!.visibility = View.GONE

        start_img!!.visibility = View.GONE
        end_img!!.visibility = View.GONE
        ta_img!!.visibility = View.GONE
        other_img!!.visibility = View.GONE


    }

    private fun calculateDistance(): Int {
        return if (startReading!!.text.toString()
                .isNotEmpty() && endReading!!.text.toString()
                .isNotEmpty()
        ) {
            val dis =
                endReading!!.text.toString().toInt() - startReading!!.text.toString()
                    .toInt()
            when (travelMode!!.text.toString()) {
                "Car" -> {
                    preExpense!!.car!! * dis
                }
                else -> {
                    0
                }
            }
        } else {
            0
        }
    }

    private fun getPreExpense() {
        val calendar: Calendar = cal!!.cal
        val back = BackgroundWork(this)
        back.execute(
            "Get Pre Expense",
            String.format("%02d", calendar.get(Calendar.MONTH) + 1),
            calendar.get(Calendar.YEAR).toString()
        )
        back.dailog!!.setOnDismissListener {
            if (back.result != "Error: Data Not Found") {

                val j = JSONObject(back.result).getJSONObject("response")

                with(preExpense!!) {
                    empId = j.getString("emp_id")
                    daHq = j.getInt("da_hq")
                    daUc = j.getInt("da_uc")
                    daNh = j.getInt("da_nh")
                    inOut = j.getInt("in_out")
                    car = j.getInt("car")
                    mobile = j.getInt("mobile")
                }

                if (preExpense!!.car != 0) {
                    val temp = tm.toMutableList()
                    temp.add("Car")
                    tm = temp.toTypedArray()
                }

                city = ArrayAdapter(this, android.R.layout.simple_list_item_1, getCity())
                getDailyExpense()
                getAttendance()

            } else {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Expense Management")
                    .setMessage("You are not Allowed to Mark Expenses ")
                    .setCancelable(false)
                    .setNeutralButton(
                        "Ok"
                    ) { dialog: DialogInterface, i: Int ->
                        finish()
                    }
                    .show()
            }
        }
    }

    private fun getAttendance() {

        attendanceList!!.clear()
        val data: MutableList<String> = ArrayList()
        val calendar: Calendar = cal!!.cal
        val back = BackgroundWork(this)
        back.execute(
            "Get Monthly Attendance",
            String.format("%02d", calendar.get(Calendar.MONTH) + 1),
            calendar.get(Calendar.YEAR).toString()
        )
        back.dailog!!.setOnDismissListener {
            if (back.result != "Error: Data Not Found") {
                val json = JSONObject(back.result)
                val jsonArray = json.optJSONArray("response")
                for (i in 0 until jsonArray!!.length()) {
                    val j_obj = jsonArray.getJSONObject(i)
                    val attendance = attendance_report()

                    attendance.settimein(j_obj.getString("Time_IN"))
                    attendance.setlocation_lat_in(j_obj.getString("Location_Lat_In"))
                    attendance.setlocation_lng_in(j_obj.getString("Location_Lng_In"))
                    attendance.settime_in_img(j_obj.getString("In_Img_Name"))

                    attendance.settimeout(j_obj.getString("Time_OUT"))
                    attendance.setlocation_lat_out(j_obj.getString("Location_Lat_Out"))
                    attendance.setlocation_lng_out(j_obj.getString("Location_Lng_Out"))
                    attendance.settime_out_img(j_obj.getString("Out_Img_Name"))

                    attendanceList!!.add(attendance)
                    data.add(j_obj.getString("Time_IN"))
                }
            }
            expenseDailyAdapter = ExpenseDailyAdapter(dailyExpenseList!!)
            expenseRecyclerview!!.adapter = expenseDailyAdapter
            cal!!.setAttendance(data)
            Log.d("Attendance Daily", attendanceList!!.size.toString())
        }
    }

    private fun getDailyExpense() {
        dailyExpenseList!!.clear()
        dailyExpenseTotal = 0
        val calendar: Calendar = cal!!.cal
        val back = BackgroundWork(this)
        back.execute(
            "Get Expense",
            String.format("%02d", calendar.get(Calendar.MONTH) + 1),
            calendar.get(Calendar.YEAR).toString()
        )
        back.dailog!!.setOnDismissListener {
            if (back.result != "Error: Data Not Found") {
                val json = JSONObject(back.result)
                val jsonArray = json.optJSONArray("response")
                for (i in 0 until jsonArray!!.length()) {
                    val j_obj = jsonArray.getJSONObject(i)
                    val expense = ExpenseDaily()
                    with(expense) {
                        empId = j_obj.getString("emp_id")
                        date = j_obj.getString("date")
                        workType =j_obj. getString("work_type")
                        townFrom = j_obj.getString("town_from")
                        townTo = j_obj.getString("town_to")
                        travelMode = j_obj.getString("travel_mode")
                        start = j_obj.getInt("start")
                        end = j_obj.getInt("end")
                        da = j_obj.getInt("da")
                        ta = j_obj.getInt("ta")
                        inOut = j_obj.getInt("in_out")
                        other = j_obj.getInt("other")
                        prefix = j_obj.getString("img_prefix")
                        status = j_obj.getString("status")

                    }
                    dailyExpenseList!!.add(expense)
                    dailyExpenseTotal =
                        dailyExpenseTotal!! + (expense.da!! + expense.ta!! + expense.inOut!! + expense.other!!)
                }
            }

            expenseDailyAdapter = ExpenseDailyAdapter(dailyExpenseList!!)
            expenseRecyclerview!!.adapter = expenseDailyAdapter
            cal!!.setEvents(dailyExpenseList)

            Log.d("Expense Daily", dailyExpenseList!!.size.toString())

        }
    }

    private fun getMonthlyExpense() {


        val calendar: Calendar = cal!!.cal
        val back = BackgroundWork(this)
        back.execute(
            "Get Monthly Expense",
            SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time).toString(),
            calendar.get(Calendar.YEAR).toString()
        )
        back.dailog!!.setOnDismissListener {
            expense = ExpenseMonthly()
            if (back.result != "Error: Data Not Found") {
                val json = JSONObject(back.result).getJSONObject("response")
                with(expense) {
                    empId = json.getString("emp_id")
                    monthYear = json.getString("month_year")
                    pass = json.getInt("travel_pass")
                    courier = json.getInt("courier_stationery")
                    sample = json.getInt("sample")
                    prefix = json.getString("img_prefix")
                    status = json.getString("status")
                }
            }
            initalize_monthly()
        }

    }

    fun getCity(): MutableList<String> {
        cityList.clear()
        val back = BackgroundWork(this)
        back.execute("City", "", "")
        back.dailog!!.setOnDismissListener {

            val json = JSONObject(back.result)
            val jsonArray = json.optJSONArray("response")

            for (i in 0 until jsonArray!!.length()) {
                val j_obj = jsonArray.getJSONObject(i)
                cityList.add(j_obj.getString("city"))
            }
        }
        return cityList
    }

    // Upload captured image online on server function.
    private fun imageUploadToServerFunction(): String? {
        bitmap = Bitmap.createScaledBitmap(bitmap!!, 786, 1024, true)

        val byteArrayOutputStreamObject = ByteArrayOutputStream()
        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject)
        var options = 90
        while (byteArrayOutputStreamObject.toByteArray().size / 1024 > 300) {  //Loop if compressed picture is greater than 400kb, than to compression
            byteArrayOutputStreamObject.reset() //Reset baos is empty baos
            bitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                byteArrayOutputStreamObject
            ) //The compression options%, storing the compressed data to the baos
            options -= 10 //Every time reduced by 10
        }

        val byteArrayVar = byteArrayOutputStreamObject.toByteArray()
        return Base64.encodeToString(byteArrayVar, Base64.DEFAULT)
    }


    /** Over-ridden methods Connections */
// Activity Result for Camera
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode % 2 == 0) {
                    //    bitmap = data.extras?.get("data") as Bitmap
                    Log.d("Path", photoFile.absolutePath)
                    bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    photoFile.delete()
                } else {
                    if (data != null) {
                        val contentURI = data.data
                        bitmap =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                contentURI?.let {
                                    ImageDecoder.createSource(
                                        this.contentResolver,
                                        it
                                    )
                                }?.let { ImageDecoder.decodeBitmap(it) }
                            } else {
                                MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                            }
                    }
                }
                when (requestCode) {

                    0, 1 -> {
                        start_img_preview_link!!.visibility = View.VISIBLE
                        start_img_string = imageUploadToServerFunction()
                        start_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        start_img_preview!!.setImageBitmap(bitmap)

                    }
                    2, 3 -> {
                        end_img_preview_link!!.visibility = View.VISIBLE
                        end_img_string = imageUploadToServerFunction()
                        end_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        end_img_preview!!.setImageBitmap(bitmap)
                    }
                    4, 5 -> {

                        ta_img_preview_link!!.visibility = View.VISIBLE
                        ta_img_string = imageUploadToServerFunction()
                        ta_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        ta_img_preview!!.setImageBitmap(bitmap)
                    }
                    6, 7 -> {

                        other_img_preview_link!!.visibility = View.VISIBLE
                        other_img_string = imageUploadToServerFunction()
                        other_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        other_img_preview!!.setImageBitmap(bitmap)
                    }
                    8, 9 -> {

                        pass_img_preview_link!!.visibility = View.VISIBLE
                        pass_img_string = imageUploadToServerFunction()
                        pass_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        pass_img_preview!!.setImageBitmap(bitmap)

                    }
                    10, 11 -> {
                        courier_img_preview_link!!.visibility = View.VISIBLE
                        courier_img_string = imageUploadToServerFunction()
                        courier_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        courier_img_preview!!.setImageBitmap(bitmap)

                    }
                    12, 13 -> {

                        sample_img_preview_link!!.visibility = View.VISIBLE
                        sample_img_string = imageUploadToServerFunction()
                        sample_img!!.setImageResource(R.drawable.ic_twotone_check_24)
                        sample_img_preview!!.setImageBitmap(bitmap)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /** over-ridden code for menu */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.expense, menu)
        menu!!.findItem(R.id.submit_expense).setActionView(R.layout.menu_button)
        menu.findItem(R.id.submit_expense).actionView!!.findViewById<Button>(R.id.process)
            .setOnClickListener {
                getMonthlyExpense()
            }
        return true
    }


    /** NEW CODE TO COMPRESS THE IMAGE */

    fun compressImage(imageUri: String): String? {
        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        /** By setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If  you try the use the bitmap here, you will get null. */
        options.inJustDecodeBounds = true

        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        // max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 786.0f
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight

        // width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            when {
                imgRatio < maxRatio -> {
                    imgRatio = maxHeight / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                }
                imgRatio > maxRatio -> {
                    imgRatio = maxWidth / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                }
                else -> {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
        }

        // setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        // this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        // check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            when (orientation) {
                6 -> {
                    matrix.postRotate(90F)
                    Log.d("EXIF", "Exif: $orientation")
                }
                 3 -> {
                    matrix.postRotate(180F)
                    Log.d("EXIF", "Exif: $orientation")
                }
                8 -> {
                    matrix.postRotate(270F)
                    Log.d("EXIF", "Exif: $orientation")
                }
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }


        val out = ByteArrayOutputStream()
        try {

            // write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val byteArrayVar = out.toByteArray()

        return Base64.encodeToString(byteArrayVar, Base64.DEFAULT)
    }

    fun getFilename(): String {
        val file = File(
            Environment.getExternalStorageDirectory().path,
            "MyFolder/Images"
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
    }


    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor: Cursor? = contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }

    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }
}