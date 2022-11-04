package com.malas.appsr.malasapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.*
import com.malas.appsr.malasapp.HomeActivity
import com.malas.appsr.malasapp.R
import com.malas.appsr.malasapp.serverconnection.BackgroundWork
import com.malas.appsr.malasapp.services.geocoder.Constants
import com.malas.appsr.malasapp.services.geocoder.FetchAddressIntentService
import com.malas.appsr.malasapp.session.SessionManagement
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ClockInOut : AppCompatActivity() {

    /***
     *Image Objects
     */
    private var img: ImageView? = null
    val PERMISSION_ID = 44
    private var bitmap: Bitmap? = null
    var convertImage: String? = null

    private val APP_TAG = "Malas_Sr_App"
    private var photoFileName = "photo.jpg"
    private lateinit var photoFile: File

    /**
     *Location Objects
     */

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var add_text: TextView
    }

    private var resultReceiver: ResultReceiver? = null
    var location: Location? = null

    /**
     * Referesh Animation
     */

    private var refresh: LinearLayout? = null
    private var refreshImg: ImageView? = null

    var session: SessionManagement? = null
    var tf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    /**
     * Clock Type and Date*
     */
    private var clockType: TextView? = null
    private var clockTypeDate: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_in_out)

        session = SessionManagement(this)

        clockType = findViewById(R.id.clock_type)
        clockTypeDate = findViewById(R.id.clock_type_date)

        if (session!!.isClockedIN()) {
            supportActionBar!!.title = "Clock Out"
            clockType!!.text = "Clock Out"
            clockTypeDate!!.text =
                " For " + SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).parse(session!!.getUserDetails()[session!!.KEY_ClockIn].toString()) as Date
                )
        } else {
            supportActionBar!!.title = "Clock In"
            clockType!!.text = "Clock In"
            clockTypeDate!!.text = " For " + SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
        }
        img = findViewById(R.id.add_img)
        add_text = findViewById(R.id.address)
        refreshImg = findViewById(R.id.refresh)
        refresh = findViewById(R.id.refresh_location)
        resultReceiver = AddressResultReceiver( Handler(Looper.getMainLooper()))

        checkLocationPermission()
        enableRuntimePermissionToAccessCamera()

        img?.setOnClickListener {
            if (add_text.text.toString() != "Data Connection Error") {
//                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                // Create a File reference for future access
                photoFile = getPhotoFileUri(photoFileName)

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

                val fileProvider: Uri =
                    FileProvider.getUriForFile(this, "com.malas.appsr.fileprovider", photoFile)

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.

                //     startActivityForResult(intent, 0)

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                } else {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
                }
                startActivityForResult(cameraIntent, 0)

            } else {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Location not found!!!")
                    .setCancelable(false)
                    .setMessage("Unable to fetch the Location. please click on 'Refresh Location'")
                    .setCancelable(false)
                    .setNeutralButton("ok") { _: DialogInterface, _: Int ->
                    }
                    .show()
            }
        }
        refresh!!.setOnClickListener {
            checkLocationPermission()
        }

    }

    private fun getPhotoFileUri(photoFileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename

        // Return the file target for the photo based on filename

        return File(mediaStorageDir.path + File.separator + photoFileName)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            if (bitmap!!.width > bitmap!!.height) {
                // Bitmap bMapRotate=null;
                val mat = Matrix()
                mat.postRotate(90F)
                bitmap = Bitmap.createBitmap(
                    bitmap!!,
                    0,
                    0,
                    bitmap!!.width,
                    bitmap!!.height,
                    mat,
                    true
                )
            }

            img?.setImageBitmap(bitmap)
            photoFile.delete()
            imageUploadToServerFunction()
            val back = BackgroundWork(this)

            if (!session!!.isClockedIN()) {
                back.execute(
                    "Clock In",
                    location!!.latitude.toString(),
                    location!!.longitude.toString(),
                    add_text.text.toString(),
                    convertImage
                )
                back.dailog!!.setOnDismissListener {
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Clock-In")
                    checksession()
                    when (back.result) {
                        "Clock-In Successful" -> {
                            //session!!.createClockInSession(time.toString(),status)
                            alert.setMessage("Attendance Marked Successfully")
                                .setCancelable(false)
                                .setNeutralButton(
                                    "ok"
                                ) { _: DialogInterface, _: Int ->
                                    finishAffinity()
                                    startActivity(Intent(this@ClockInOut, HomeActivity::class.java))
                                }
                        }
                        "Clock-In Pending" -> {
                            //session!!.createClockInSession(time.toString(),status)

                            alert.setMessage("Your Clock-In Is Pending as You are Late For the day. Please Contact your Manager.")
                                .setCancelable(false)
                                .setNeutralButton("ok") { _: DialogInterface, _: Int ->
                                    finishAffinity()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                        }
                        else -> {
                            alert.setMessage("Attendance Not Marked Due To Following Reason :\n" + back.result)
                                .setCancelable(true)
                        }
                    }
                    alert.show()
                }
            } else {

                back.execute(
                    "Clock Out",
                    location!!.latitude.toString(),
                    location!!.longitude.toString(),
                    add_text.text.toString(),
                    convertImage
                )
                back.dailog!!.setOnDismissListener {
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Clock-Out")
                    checksession()
                    when (back.result) {
                        "Clock-Out Successful" -> {
                            //session!!.createClockOutSession(time.toString(),status)
                            alert.setMessage("Clocked-Out Successful")
                                .setCancelable(false)
                                .setNeutralButton("ok") { _: DialogInterface, _: Int ->
                                    finishAffinity()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                        }
                        "Clock-Out Pending" -> {
                            //session!!.createClockOutSession(time.toString(),status)
                            alert.setMessage("Your Attendance Is Pending as You have marked it Late. Please Contact your Manager.")
                                .setCancelable(false)
                                .setNeutralButton("ok") { _: DialogInterface, _: Int ->
                                    finishAffinity()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                        }
                        else -> {
                            alert.setMessage("Clocked out Failed Due To Unknown Reason")
                                .setCancelable(true)
                        }
                    }
                    alert.show()

                }
            }
        }
    }

    /**
     * Run Time Permission for Camera App
     */
    private fun enableRuntimePermissionToAccessCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(
                applicationContext,
                "CAMERA permission allows us to Access CAMERA app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_ID
            )
        }
    }

    /**
     *  Convert Image Into String.
     *  to
     *  Upload on Server
     */
    private fun imageUploadToServerFunction() {
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
        convertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT)


    }


    /**
     *Handling Permission Result
     */
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        } else {
            getcurrentlocation()
        }

    }


    /**
     *Handling Permission Result
     */

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentlocation()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }

    }


    /**
     *Getting Current Location using Gps
     */

    private fun getcurrentlocation() {

        if (!isLocationEnabled()) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Location")
                .setMessage("Turn On The Location.")
                .setCancelable(false)
                .setPositiveButton("yes") { _: DialogInterface, _: Int ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                .show()
        }


        val rotate: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_clockwise)
        refreshImg!!.startAnimation(rotate)

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            fastestInterval = 3000
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    LocationServices.getFusedLocationProviderClient(this@ClockInOut)
                        .removeLocationUpdates(this)

                    if (locationResult.locations.size > 0) {
                        val latestLocationIndex = locationResult.locations.size - 1
                        val lat = locationResult.locations[latestLocationIndex].latitude
                        val lng = locationResult.locations[latestLocationIndex].longitude
                        location = Location("ProviderNA")
                        location!!.latitude = lat
                        location!!.longitude = lng
                        fetchaddressfromlatlng(location!!)

                    }
                }
            }, Looper.getMainLooper()
        )
    }


    /**
     *Sending Location To Convert Into Address
     */

    fun fetchaddressfromlatlng(location: Location) {
        val i = Intent(this, FetchAddressIntentService::class.java)
        i.putExtra(Constants.RECEIVER, resultReceiver)
        i.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        startService(i)
    }

    /**
     * Check if Location Service are Enable or Not
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    /**
     *Overriding Result Receiver Class
     */

    class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            if (resultCode == Constants.SUCESS_RESULT) {
                val address: String? = resultData!!.getString(Constants.RESULT_DATA_KEY)
                add_text.text = address!!.replace("\"", "")

            } else {
                add_text.text = "Data Connection Error"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    /**
     * Check Clock-In/Clock-Out Session
     */

    private fun checksession() {
        val back = BackgroundWork(this)
        back.execute("Check Session")
        back.dailog!!.setOnDismissListener {
            when (back.result) {
                "Your Account has been Deactivated" -> {
                    finishAffinity()
                    session!!.logoutUser()
                }
                "No session Found" -> {
                    session!!.clearSession()
                }
                else -> {
                    val j = JSONObject(back.result).getJSONObject("response")

                    val timein = j.getString("Time_IN")
                    val timeout = j.getString("Time_OUT")
                    val status = j.getString("Status")

                    session!!.createClockInSession(timein, status)

                    if (timeout != "null") {
                        session!!.createClockOutSession(timeout, status)
                    }
                }
            }
        }
    }
}