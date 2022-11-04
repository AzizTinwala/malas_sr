package com.malas.appsr.malasapp.serverconnection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import com.Amitlibs.utils.ComplexPreferences
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean
import com.malas.appsr.malasapp.BuildConfig
import com.malas.appsr.malasapp.Constant
import com.malas.appsr.malasapp.R
import com.malas.appsr.malasapp.session.SessionManagement
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Calendar


class BackgroundWork(ctx: Context) : AsyncTask<String?, Unit, String?>() {

    @SuppressLint("StaticFieldLeak")
    var context: Context? = null
    var alt: AlertDialog.Builder? = null
    var dailog: AlertDialog? = null
    var type: String? = ""
    var result = ""
    var time: String? = null
    var mUserLoginInfoBean: UserLoginInfoBean? = null
    var session: SessionManagement? = null


    init {
        context = ctx

    }

    override fun doInBackground(vararg params: String?): String {

        type = params[0]
        var id = mUserLoginInfoBean!!.userId.toString()
        val desig = mUserLoginInfoBean!!.designationId.toString()

        /** BASE  URL */
        var register_url = Constant.BASE_URL2

        var post = ""


        try {


            when (type) {

                /**
                 * Check Clock-In / Clock-Out Session
                 */

                "Check Session" -> {
                    register_url += "session.php"
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    )
                }

                /**
                 * Clock In for the Day
                 */

                "Clock In" -> {
                    register_url += "clock_in.php"
                    val location_lat: String? = params[1]
                    val location_long: String? = params[2]
                    val location_address: String? = params[3]
                    val img = params[4]

                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("location_lat", "UTF-8") + "=" + URLEncoder.encode(
                        location_lat,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("location_long", "UTF-8") + "=" + URLEncoder.encode(
                        location_long,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode(
                        "location_address",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode(
                        location_address,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(
                        img,
                        "UTF-8"
                    )
                }


                /**
                 * Clock Out for the Day
                 */

                "Clock Out" -> {
                    register_url += "clock_out.php"
                    val time_in = session!!.getUserDetails()[session!!.KEY_ClockIn].toString()
                    val location_lat = params[1]
                    val location_long = params[2]
                    val location_address = params[3]
                    val img = params[4]

                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("time_in", "UTF-8") + "=" + URLEncoder.encode(
                        time_in,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("location_lat", "UTF-8") + "=" + URLEncoder.encode(
                        location_lat,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("location_long", "UTF-8") + "=" + URLEncoder.encode(
                        location_long,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode(
                        "location_address",
                        "UTF-8"
                    ) + "=" + URLEncoder.encode(
                        location_address,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(
                        img,
                        "UTF-8"
                    )
                }

                /** User Monthly Attendance Details */

                "Get Monthly Attendance" -> {
                    register_url += "get_monthly_attendance.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month",    "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }


                /**
                 * Get City List
                 */

                "City" -> {

                    register_url += "getcity.php"
                    val state = params[1]
                    val district = params[2]
                    post = URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(
                        state,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("district", "UTF-8") + "=" + URLEncoder.encode(
                        district,
                        "UTF-8"
                    )
                }


                /** Get Holiday List */

                "Get Holiday" -> {
                    register_url += "getholiday.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    )
                }
                /** User Pre-Define Expense Details */

                "Get Pre Expense" -> {

                    register_url += "get_predefine_expense.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )

                }
                /** User Save Daily Expense Details */

                "Daily Expense" -> {

                    register_url += "daily_expense.php"

                    val date = params[1]
                    val work_type = params[2]
                    val from = params[3]
                    val to = params[4]
                    val travel_mode = params[5]
                    val start = params[6]
                    val end = params[7]
                    val ta = params[8]
                    val inout = params[9]
                    val other = params[10]
                    val start_img = params[11]
                    val end_img = params[12]
                    val ta_img = params[13]
                    val other_img = params[14]

                    /* post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                         id,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(
                         date,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("work_type", "UTF-8") + "=" + URLEncoder.encode(
                         work_type,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(
                         from,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(
                         to,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("travel_mode", "UTF-8") + "=" + URLEncoder.encode(
                         travel_mode,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("start", "UTF-8") + "=" + URLEncoder.encode(
                         start,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("end", "UTF-8") + "=" + URLEncoder.encode(
                         end,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("ta", "UTF-8") + "=" + URLEncoder.encode(
                         ta,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("inout", "UTF-8") + "=" + URLEncoder.encode(
                         inout,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("other", "UTF-8") + "=" + URLEncoder.encode(
                         other,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("start_img", "UTF-8") + "=" + URLEncoder.encode(
                         start_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("end_img", "UTF-8") + "=" + URLEncoder.encode(
                         end_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("ta_img", "UTF-8") + "=" + URLEncoder.encode(
                         ta_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("other_img", "UTF-8") + "=" + URLEncoder.encode(
                         other_img,
                         "UTF-8"
                     )*/
                    val temp = JSONObject()
                    temp.put("action", type)
                    temp.put("id", id)
                    temp.put("desig", desig)
                    temp.put("date", date)
                    temp.put("work_type", work_type)
                    temp.put("from", from)
                    temp.put("to", to)
                    temp.put("travel_mode", travel_mode)
                    temp.put("start", start)
                    temp.put("end", end)
                    temp.put("ta", ta)
                    temp.put("inout", inout)
                    temp.put("other", other)
                    temp.put("start_img", start_img)
                    temp.put("end_img", end_img)
                    temp.put("ta_img", ta_img)
                    temp.put("other_img", other_img)
                    post = temp.toString()
                }

                /** User Update Daily Expense Details */

                "Update Daily Expense" -> {

                    register_url += "update_daily_expense.php"

                    val date = params[1]
                    val work_type = params[2]
                    val from = params[3]
                    val to = params[4]
                    val travel_mode = params[5]
                    val start = params[6]
                    val end = params[7]
                    val ta = params[8]
                    val inout = params[9]
                    val other = params[10]
                    val start_img = params[11]
                    val end_img = params[12]
                    val ta_img = params[13]
                    val other_img = params[14]

                    /* post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                         id,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(
                         date,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("work_type", "UTF-8") + "=" + URLEncoder.encode(
                         work_type,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(
                         from,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(
                         to,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("travel_mode", "UTF-8") + "=" + URLEncoder.encode(
                         travel_mode,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("start", "UTF-8") + "=" + URLEncoder.encode(
                         start,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("end", "UTF-8") + "=" + URLEncoder.encode(
                         end,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("ta", "UTF-8") + "=" + URLEncoder.encode(
                         ta,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("inout", "UTF-8") + "=" + URLEncoder.encode(
                         inout,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("other", "UTF-8") + "=" + URLEncoder.encode(
                         other,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("start_img", "UTF-8") + "=" + URLEncoder.encode(
                         start_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("end_img", "UTF-8") + "=" + URLEncoder.encode(
                         end_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("ta_img", "UTF-8") + "=" + URLEncoder.encode(
                         ta_img,
                         "UTF-8"
                     ) + "&" + URLEncoder.encode("other_img", "UTF-8") + "=" + URLEncoder.encode(
                         other_img,
                         "UTF-8"
                     )*/
                    val temp = JSONObject()
                    temp.put("action", type)
                    temp.put("id", id)
                    temp.put("desig", desig)
                    temp.put("date", date)
                    temp.put("work_type", work_type)
                    temp.put("from", from)
                    temp.put("to", to)
                    temp.put("travel_mode", travel_mode)
                    temp.put("start", start)
                    temp.put("end", end)
                    temp.put("ta", ta)
                    temp.put("inout", inout)
                    temp.put("other", other)
                    temp.put("start_img", start_img)
                    temp.put("end_img", end_img)
                    temp.put("ta_img", ta_img)
                    temp.put("other_img", other_img)
                    post = temp.toString()
                }

                /** User Monthly Expense Details */

                "Monthly Expense" -> {
                    register_url += "monthly_expense.php"

                    val date = params[1]
                    val travel = params[2]
                    val courier = params[3]
                    val sample = params[4]
                    val pass_img = params[5]
                    val courier_img = params[6]
                    val sample_img = params[7]
                    val status = params[8]
                    val type = params[9]

                    /*post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(
                        date,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("travel", "UTF-8") + "=" + URLEncoder.encode(
                        travel,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("courier", "UTF-8") + "=" + URLEncoder.encode(
                        courier,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("sample", "UTF-8") + "=" + URLEncoder.encode(
                        sample,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("pass_img", "UTF-8") + "=" + URLEncoder.encode(
                        pass_img,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("courier_img", "UTF-8") + "=" + URLEncoder.encode(
                        courier_img,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("sample_img", "UTF-8") + "=" + URLEncoder.encode(
                        sample_img,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(
                        status,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    )*/
                    val temp = JSONObject()
                    temp.put("action", type)
                    temp.put("id", id)
                    temp.put("date", date)
                    temp.put("travel", travel)
                    temp.put("courier", courier)
                    temp.put("sample", sample)
                    temp.put("pass_img", pass_img)
                    temp.put("courier_img", courier_img)
                    temp.put("sample_img", sample_img)
                    temp.put("status", status)
                    temp.put("desig", desig)
                    temp.put("type", type)
                    post = temp.toString()
                }

                /** User Daily  Expense Details */

                "Get Expense" -> {
                    register_url += "get_expense.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }
                /** User Monthly  Expense Details */

                "Get Monthly Expense" -> {
                    register_url += "get_monthly_expense.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }
                /** Final Submit  Expense Details */

                "Submit Expense" -> {
                    register_url += "final_expense.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }

                /**
                 * Check Leave Eligiblity
                 */

                "Check Leave Eligibility" -> {
                    register_url =
                        "https://app.malasportal.in/MALASRSM/LeaveController.php"
                    val uid: String? = params[1]
                    val desig: String? = params[2]
                    val fDate: String? = params[3]
                    val tDate = params[4]

                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                        uid,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig_id", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("start_date", "UTF-8") + "=" + URLEncoder.encode(
                        fDate,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(
                        tDate,
                        "UTF-8"
                    )
                }

                /**
                 * Check ClockIn Eligiblity
                 */

                "Check ClockIn Eligibility" -> {

                    register_url += "check_revert.php"
                    val uid: String? = params[1]
                    val desig: String? = params[2]
                    val fDate: String? = params[3]
                    val tDate = params[4]

                    post = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(
                        uid,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("fdate", "UTF-8") + "=" + URLEncoder.encode(
                        fDate,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("tdate", "UTF-8") + "=" + URLEncoder.encode(
                        tDate,
                        "UTF-8"
                    )
                }

                /** Check Admin/ Other Work Eligiblity */

                "Admin/Other Work Eligibility" -> {
                    register_url += "check_admin_other_eligibility.php"

                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    )
                }

                /** Get Leave Reason's */

                "Get Leave Reason" -> {
                    register_url += "get_leave_reason.php"
                }

                /** Get Joint Work Name's */

                "Get JointWork Name" -> {
                    register_url += "getJointWorkName.php"
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    )
                }

                /** User Monthly Leave Details */

                "Get Monthly Leave" -> {
                    register_url += "get_monthly_leave_report.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }

                /** Salary Monthly Details */

                "Get Salary Month" -> {
                    register_url += "getSalarySlipMonth.php"
                    id = mUserLoginInfoBean!!.empCode
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    )
                }

                /** Submit Leave Request*/

                "Submit Leave Request" -> {

                    register_url = "https://app.malasportal.in/MALASRSM/LeaveController.php"

                    val start: String? = params[1]
                    val end: String? = params[2]
                    val days: String? = params[3]
                    val reason: String? = params[4]

                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig_id", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("start_date", "UTF-8") + "=" + URLEncoder.encode(
                        start,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(
                        end,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("days", "UTF-8") + "=" + URLEncoder.encode(
                        days,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(
                        reason,
                        "UTF-8"
                    )

                }

                /** Submit Comp-Off Request */

                "Submit Compoff Request" -> {
                    register_url = "https://app.malasportal.in/MALASRSM/LeaveController.php"
                    val start: String? = params[1]
                    val end: String? = params[2]
                    val days: String? = params[3]
                    val reason: String? = params[4]
                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig_id", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("start_date", "UTF-8") + "=" + URLEncoder.encode(
                        start,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(
                        end,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("days", "UTF-8") + "=" + URLEncoder.encode(
                        days,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(
                        reason,
                        "UTF-8"
                    )
                }

                /** Submit Leave Revert Request*/

                "Submit Leave Revert Request" -> {
                    register_url = "https://app.malasportal.in/MALASRSM/LeaveController.php"

                    val lvid: String? = params[1]
                    val start: String? = params[2]
                    val end: String? = params[3]
                    val days: String? = params[4]
                    val leave_type: String? = params[5]

                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("lvid", "UTF-8") + "=" + URLEncoder.encode(
                        lvid,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig_id", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("start_date", "UTF-8") + "=" + URLEncoder.encode(
                        start,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(
                        end,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("days", "UTF-8") + "=" + URLEncoder.encode(
                        days,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("leave_type", "UTF-8") + "=" + URLEncoder.encode(
                        leave_type,
                        "UTF-8"
                    )
                }


                /** Upload User Image */

                "Update Image Profile" -> {
                    register_url += "uploadImage.php"
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("user_img", "UTF-8") + "=" + URLEncoder.encode(
                        params[1],
                        "UTF-8"
                    )
                }

                /**
                 * Active App Version OR NOT
                 */

                "Check App Version" -> {
                    register_url += "checkAppVersion.php"
                    val version = BuildConfig.VERSION_NAME
                    post =  URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode(
                        version,
                        "UTF-8"
                    )
                }


                /** User Monthly Absent Details */

                "Get Monthly Absent" -> {
                    register_url += "getAbsent.php"
                    val month: String? = params[1]
                    val year: String? = params[2]
                    post = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }

                /** User Weekly Tour Plan */

                "Get Weekly Tour Plan" -> {
                    register_url = "https://app.malasportal.in/expense/TourPlanController.php"
                    val week: String? = params[1]
                    val month: String = (Calendar.getInstance().get(Calendar.MONTH)+1).toString()
                    val year: String =  (Calendar.getInstance().get(Calendar.YEAR)).toString()
                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) /*+ "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(
                        id,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("desig_id", "UTF-8") + "=" + URLEncoder.encode(
                        desig,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("week", "UTF-8") + "=" + URLEncoder.encode(
                        week,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )*/
                }


                /** Insert Tour Plan Day wise */

                "Insert Tour Plan" -> {
                    register_url += "TourPlanController.php"
                    val month: String = (Calendar.getInstance().get(Calendar.MONTH)+1).toString()
                    val year: String =  (Calendar.getInstance().get(Calendar.YEAR)).toString()
                    post = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(
                        type,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(
                        month,
                        "UTF-8"
                    ) + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(
                        year,
                        "UTF-8"
                    )
                }
            }


            val url = URL(register_url)
            val http = url.openConnection() as HttpURLConnection
            http.connectTimeout = 60000
            http.doOutput = true
            http.doInput = true
            http.requestMethod = "POST"
            val outputStream = http.outputStream
            val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
            bufferedWriter.write(post)
            bufferedWriter.flush()
            bufferedWriter.close()
            outputStream.close()
            val inputStream = http.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            for (line in bufferedReader.readLine()) {
                result += line
            }

            bufferedReader.close()
            inputStream.close()
            http.disconnect()

        } catch (e: Exception) {
            Log.d("Error", e.toString())
        } finally {
            Log.d("Result", result)
            return result
        }
    }

    override fun onPreExecute() {

        session = SessionManagement(context as Context)

        val complexPreferences = ComplexPreferences.getComplexPreferences(
            context,
            Constant.UserRegInfoPref,
            Context.MODE_PRIVATE
        )
        mUserLoginInfoBean =
            complexPreferences.getObject(Constant.UserRegInfoObj, UserLoginInfoBean::class.java)
        alt = AlertDialog.Builder(context)
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dv = inflater.inflate(R.layout.progess_dialoge, null)
        alt!!.setView(dv)
        alt!!.setCancelable(false)

        dailog = alt!!.create()
        dailog!!.show()

    }

    override fun onPostExecute(result: String?) {
        alt!!.setMessage(result.toString())
        dailog!!.dismiss()
    }
}