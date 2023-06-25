package com.malas.appsr.malasapp.activities

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.malas.appsr.malasapp.R

class Catalogue : AppCompatActivity() {


    var web: WebView? = null
    var downloadManager: DownloadManager? = null
    val file = "https://app.malasportal.in/catalogue/catalogue.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)
        supportActionBar!!.title = "Catalogue"
        val p = findViewById<ProgressBar>(R.id.catalogue_progress)
        p.visibility = View.INVISIBLE

        web = findViewById(R.id.catalogue_webview)
        web!!.loadUrl(
            "http://docs.google.com/gview?embedded=true&url=$file"
        )
        val ws = web!!.settings
        ws.javaScriptEnabled = true
        ws.setSupportZoom(true)
        web!!.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                p.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                p.visibility = View.INVISIBLE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        menu!!.getItem(0).setIcon(R.drawable.ic_baseline_cloud_download_24)
        menu.getItem(0).title = "Download"
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Logout -> {
                downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse(file)
                val request = DownloadManager.Request(uri)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                downloadManager!!.enqueue(request)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}