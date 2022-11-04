package com.malas.appsr.malasapp.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.Amitlibs.utils.ComplexPreferences
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean
import com.malas.appsr.malasapp.Constant
import com.malas.appsr.malasapp.HomeActivity
import java.text.SimpleDateFormat
import java.util.*

class SessionManagement(context: Context) {
    //Shared Preferences
    var pref: SharedPreferences? = null

    //Editor For Shared Preferences
    var editor: SharedPreferences.Editor? = null

    //Context
    var _context: Context? = null

    //Shared Preferences Mode
    var PRIVATE_MODE: Int = 0

    //Shared Preferences File Name
    private val PREF_NAME = "MalasFruit"

    //All Shared Preferences Keys
    //Login Key
    private val IS_LOGIN = "IsLoggedIn"

    //Attendance Key
    private val IS_CLOCKIN = "IsClockIn"

    //User Name
    val KEY_ID = "id"

    //User Name
    val KEY_NAME = "name"

    //User Email
    val KEY_EMAIL = "email"

    //User Department
    val KEY_DEPARTMENT = "department"

    //User Designation
    val KEY_DESIGNATION = "designation"

    //Clock IN
    val KEY_ClockIn = "clockin"

    //Clock Out
    val KEY_ClockOut = "clockout"

    //Clock Out
    val KEY_AttendanceStatus = "attendancestatus"

    //Time In
    val KEY_TimeIn = "timein"

    //Time Out
    val KEY_TimeOut = "timeout"

    //Time In
    val KEY_Location = "location"

    //Time Out
    val KEY_Weekoff = "weekoff"

    //Is Birthday Today
    val IS_BIRTHDAY = "isBirthday"

    //Birthday Visiblity
    val IS_BIRTHDAY_VISIBLE = "isBirthdayVisible"

    /**
     * Initalizing the Variables
     */
    init {
        this._context = context
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref?.edit()
    }

    /**
     * Creating A Login  Session
     * */

    fun createLoginSession(
        id: String,
        name: String,
        email: String,
        department: String,
        designation: String,
        timein: String,
        timeout: String,
        location: String,
        weekoff: String
    ) {

        //Storing Login Value As TRUE
        editor!!.putBoolean(IS_LOGIN, true)

        //Storing ID in PREF
        editor!!.putString(KEY_ID, id)

        //Storing name in PREF
        editor!!.putString(KEY_NAME, name)

        //Storing Email in PREF
        editor!!.putString(KEY_EMAIL, email)

        //Storing designation in PREF
        editor!!.putString(KEY_DESIGNATION, designation)

        //Storing department in PREF
        editor!!.putString(KEY_DEPARTMENT, department)

        //Storing Time-IN in PREF
        editor!!.putString(KEY_TimeIn, timein)

        //Storing Time_Out in PREF
        editor!!.putString(KEY_TimeOut, timeout)

        //Storing Location Access Type in PREF
        editor!!.putString(KEY_Location, location)

        //Storing Week Off in PREF
        editor!!.putString(KEY_Weekoff, weekoff)

        //Commit Changes
        editor!!.commit()
    }

    /**
     * Check Login Method Will Check User Login Status
     * if False it will Redirect user to Login pages
     * Else Won't do anything
     */

    private fun checkLogin() {
        if (!isLoggedIn()) {

            //User is not logged in redirect to Login Page
            val i = Intent(_context, HomeActivity::class.java)

            //Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            //Start new Activity
            _context!!.startActivity(i)

        } else {

            //User is not logged in redirect to Login Page
            val i = Intent(_context, HomeActivity::class.java)

            //Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            //Start new Activity
            _context!!.startActivity(i)

        }
    }

    /**
     * Get stored Session Data
     * */

    fun getUserDetails(): HashMap<String, String?> {

        val user: HashMap<String, String?> = HashMap()


        //User id
        user[KEY_ID] = pref!!.getString(KEY_ID, null)


        //User Name
        user[KEY_NAME] = pref!!.getString(KEY_NAME, null)


        //User Designation
        user[KEY_DESIGNATION] = pref!!.getString(KEY_DESIGNATION, null)

        //User Department
        user[KEY_DEPARTMENT] = pref!!.getString(KEY_DEPARTMENT, null)

        //User Email
        user[KEY_EMAIL] = pref!!.getString(KEY_EMAIL, null)

        //User ClockIN
        user[KEY_ClockIn] = pref!!.getString(KEY_ClockIn, null)

        //User ClockOut
        user[KEY_ClockOut] = pref!!.getString(KEY_ClockOut, null)

        //User ClockOut
        user[KEY_AttendanceStatus] = pref!!.getString(KEY_AttendanceStatus, null)

        //User TimeIn
        user[KEY_TimeIn] = pref!!.getString(KEY_TimeIn, null)

        //User TimeOut
        user[KEY_TimeOut] = pref!!.getString(KEY_TimeOut, null)

        //User Location
        user[KEY_Location] = pref!!.getString(KEY_Location, null)

        //User Week-Off
        user[KEY_Weekoff] = pref!!.getString(KEY_Weekoff, null)

        //Return User
        return user

    }

    /**
     * Clear Session Details*
     */

    fun logoutUser() {
        //Clearing All Data From SharedPreferences
        editor!!.clear()
        editor!!.commit()
        checkLogin()
    }

    /** Quick Check For Login */

    fun isLoggedIn(): Boolean {
        return pref!!.getBoolean(IS_LOGIN, false)
    }

    /** Quick Check For ClockIn */

    fun isClockedIN(): Boolean {
        return pref!!.getBoolean(IS_CLOCKIN, false)
    }

    /** Creating A ClockIn  Session */

    fun createClockInSession(ClockIn: String, status: String) {

        //Storing Clock in  Value As TRUE
        editor!!.putBoolean(IS_CLOCKIN, true)


        //Storing ID in PREF
        editor!!.putString(KEY_ClockIn, ClockIn)

        //Storing Clock out Time To Null
        editor!!.putString(KEY_ClockOut, null)

        //Storing Attendance Status to PREF
        editor!!.putString(KEY_AttendanceStatus, status)

        //Commit Changes
        editor!!.commit()
    }


    /** Create ClockOut Details */

    fun createClockOutSession(ClockOut: String, status: String) {

        //Storing Login Value As TRUE
        editor!!.putBoolean(IS_CLOCKIN, false)

        //Storing ID in PREF
        editor!!.putString(KEY_ClockOut, ClockOut)

        //Storing Attendance Status to PREF
        editor!!.putString(KEY_AttendanceStatus, status)

        //Commit Changes
        editor!!.commit()
    }

    /** clear ClockIn/ClockOut Details */

    fun clearSession() {
        //Storing ID in PREF
        editor!!.putString(KEY_ClockIn, null)

        //Storing Login Value As TRUE
        editor!!.putBoolean(IS_CLOCKIN, false)

        //Storing ID in PREF
        editor!!.putString(KEY_ClockOut, null)

        //Storing Attendance Status to PREF
        editor!!.putString(KEY_AttendanceStatus, null)

        //Commit Changes
        editor!!.commit()

    }


    fun isBirthday(): Boolean {
        val complexPreferences = ComplexPreferences.getComplexPreferences(
            _context,
            Constant.UserRegInfoPref,
            Context.MODE_PRIVATE
        )
        val mUserLoginInfoBean = complexPreferences.getObject(
            Constant.UserRegInfoObj,
            UserLoginInfoBean::class.java
        )

        val sdf = SimpleDateFormat("MM-dd", Locale.getDefault())
        Log.d(
            "DOB", sdf.format(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                    mUserLoginInfoBean.userDob
                ) as Date
            )
        )
        Log.d(
            "Today",
            sdf.format(Calendar.getInstance().time)

        )
        return if (sdf.format(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                    mUserLoginInfoBean.userDob
                ) as Date
            ) == sdf.format(Calendar.getInstance().time)
        ) {
            editor!!.putBoolean(IS_BIRTHDAY, true)
            //Commit Changes
            editor!!.commit()
            true
        } else {
            editor!!.putBoolean(IS_BIRTHDAY, false)

            editor!!.putBoolean(IS_BIRTHDAY_VISIBLE, true)
            //Commit Changes
            editor!!.commit()
            false
        }
    }

    fun showBirthday(): Boolean {

        return (pref!!.getBoolean(IS_BIRTHDAY, false)
                && pref!!.getBoolean(
            IS_BIRTHDAY_VISIBLE,
            true
        )
                )
    }

    fun dontShowBirthday() {
        editor!!.putBoolean(IS_BIRTHDAY_VISIBLE, false)
        //Commit Changes
        editor!!.commit()

    }
    fun initBirthday() {
        editor!!.putBoolean(IS_BIRTHDAY_VISIBLE, true)
        //Commit Changes
        editor!!.commit()

    }
}