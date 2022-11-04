package com.malas.appsr.malasapp.activities

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.Amitlibs.utils.ComplexPreferences
import com.malas.appsr.malasapp.BeanClasses.UserLoginInfoBean
import com.malas.appsr.malasapp.Constant
import com.malas.appsr.malasapp.HomeActivity
import com.malas.appsr.malasapp.R
import com.malas.appsr.malasapp.session.SessionManagement

class BirthdayGreetings : AppCompatActivity() {

    lateinit var mPlayer: MediaPlayer
    lateinit var bDayView: ImageButton
    lateinit var session: SessionManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday_greetings)

        supportActionBar!!.hide()

        mPlayer = MediaPlayer.create(this, R.raw.happy_birthday_to_you)
        session = SessionManagement(this)

        mPlayer.isLooping = true
        mPlayer.setVolume(100F, 100F)

        bDayView = findViewById(R.id.bday_view)
        bDayView.setOnClickListener {

            // The toggle is disabled
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Birthday Greetings")
                .setCancelable(true)
                .setPositiveButton(
                    "Close"
                ) { _: DialogInterface, _: Int ->
                    finish()
                }
                .setNegativeButton(
                    "Don't Show Again"
                ) { _: DialogInterface, _: Int ->
                    finish()
                    session.dontShowBirthday()
                }
                .show()

        }
    }

    override fun onResume() {
        super.onResume()
        mPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer.release()
    }
}