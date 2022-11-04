package com.malas.appsr.malasapp.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expenseutility.helper.TouchImageView
import com.malas.appsr.malasapp.R

class ImageZoomPreview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_zoom_preview)
        supportActionBar!!.hide()
        val bytes = intent.getByteArrayExtra("IMAGE_BYTES")
        val bmp: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        findViewById<TouchImageView>(R.id.zoom_img).setImageBitmap(bmp)
        findViewById<TouchImageView>(R.id.zoom_img).setMaxZoom(15f)

    }
}