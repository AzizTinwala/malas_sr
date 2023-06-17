package com.malas.appsr.malasapp.activities.salaryslip

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.malas.appsr.malasapp.BeanClasses.salaryslipmonth
import com.malas.appsr.malasapp.R
import com.malas.appsr.malasapp.serverconnection.BackgroundWork
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SalarySlipPreview : AppCompatActivity() {
    var webview: WebView? = null
    var progress: ProgressBar? = null
    var listview: AutoCompleteTextView? = null
    var download: Button? = null
    var salMonth: MutableList<salaryslipmonth>? = ArrayList()
    var month: MutableList<String>? = ArrayList()
    var downloadManager: DownloadManager? = null
    var url: String? = null
    var alt: AlertDialog.Builder? = null
    var dailog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_salary_slip_preview)
        supportActionBar!!.title = "Salary Slip"

        webview = findViewById(R.id.salary_slip_webview)
        progress = findViewById(R.id.salary_slip_progress)
        listview = findViewById(R.id.salary_month_list)
        download = findViewById(R.id.salary_slip_download)

        progress!!.visibility = View.GONE

        listview?.isFocusable = false
        listview?.isFocusableInTouchMode = false
        listview?.isCursorVisible = false
        val mon = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            getSalaryMonth() as List<String>
        )

        listview!!.setAdapter(mon)

        listview!!.setOnClickListener {
            listview!!.showDropDown()
        }

        listview!!.setOnItemClickListener { _, _, i, _ ->
            findViewById<RelativeLayout>(R.id.salary_slip_layout).visibility = View.VISIBLE
            findViewById<TextView>(R.id.salary_slip_notification).visibility = View.GONE
            progress()
            webview!!.clearCache(true)
            webview!!.clearView()
            webview!!.loadUrl("javascript:document.open();document.close();")
            val url_old = "https://malasportal.in/HR/dompdf/index.php?id= ${salMonth!![i].id}"
            val url_new =
                "https://erp.malasportal.in/HR/dompdf/new_salary_slip.php?id=${salMonth!![i].id}"

            val dateFormat = SimpleDateFormat("dd-MMM-yy", Locale.US)
            Log.e(
                "Salary Slip", "onCreate: ${
                    dateFormat.parse(
                        "01-${salMonth!![i].month!!.substring(0, 3)}-20${
                            salMonth!![i].month!!.substring(
                                (salMonth!![i].month!!.length - 2),
                                salMonth!![i].month!!.length
                            )
                        }"
                    )
                }"
            )
            url =
                if (dateFormat.parse(
                        "01-${salMonth!![i].month!!.substring(0, 3)}-20${
                            salMonth!![i].month!!.substring(
                                (salMonth!![i].month!!.length - 2),
                                salMonth!![i].month!!.length
                            )
                        }"
                    )!!
                    > dateFormat.parse("31-Mar-2023")
                ) {
                    url_new
                } else {
                    url_old
                }  //   progress!!.visibility = View.VISIBLE
            loadSalarySlip()

        }




        download!!.setOnClickListener {
            downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            downloadManager!!.enqueue(request)


        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadSalarySlip() {
        progress!!.visibility = View.VISIBLE
        webview!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress!!.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progress!!.visibility = View.INVISIBLE
                dailog!!.hide()

            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

        webview!!.settings.setSupportZoom(true)
        webview!!.settings.javaScriptEnabled = true
        webview!!.settings.loadWithOverviewMode = true
        webview!!.settings.allowFileAccess = true
        webview!!.settings.allowContentAccess = true
        webview!!.settings.allowFileAccessFromFileURLs = true
        webview!!.settings.allowUniversalAccessFromFileURLs = true
        webview!!.settings.domStorageEnabled = true
        webview!!.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        progress!!.visibility = View.GONE

/*
        try {


            Toast.makeText(baseContext, "Opening PDF... ", Toast.LENGTH_SHORT).show()
            val inte = Intent(Intent.ACTION_VIEW)
            inte.setDataAndType(
                Uri.parse(url),
                "application/pdf"
            )
            startActivity(inte)
        } catch (e: ActivityNotFoundException) {
            Log.e("Viewer not installed.", e.message.toString())
        }*/

    }

    private fun getSalaryMonth(): MutableList<String>? {
        salMonth!!.clear()
        val back = BackgroundWork(this)
        back.execute("Get Salary Month")
        back.dailog!!.setOnDismissListener {

            if (back.result != "Error: Data Not Found") {

                val json = JSONObject(back.result)

                val jsonArray = json.optJSONArray("response")

                for (i in 0 until jsonArray!!.length()) {

                    val sal = salaryslipmonth()
                    val j_obj = jsonArray.getJSONObject(i)
                    sal.id = j_obj.getString("id")
                    sal.month = j_obj.getString("month")
                    salMonth!!.add(sal)
                    month!!.add(sal.month.toString())
                }
            }
        }
        return month
    }

    fun progress() {
        alt = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dv = inflater.inflate(R.layout.progess_dialoge, null)
        alt!!.setView(dv)
        alt!!.setCancelable(false)
        dailog = alt!!.create()
        dailog!!.show()
    }
}