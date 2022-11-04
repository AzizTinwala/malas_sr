package com.malas.appsr.malasapp.services.geocoder

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import java.util.*

class FetchAddressIntentService : IntentService("FetchAddressIntentService") {

    private var resultreciver: ResultReceiver? = null
    override fun onHandleIntent(intent: Intent?) {

        if (intent != null) {
            var errorMessage = ""
            resultreciver = intent.getParcelableExtra(Constants.RECEIVER)
            val location:Location?=
                    intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA)

            val geocoder = Geocoder(this, Locale.getDefault())
            var addresses: MutableList<Address>? = null
            try {
                addresses = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

            if (addresses == null || addresses.isEmpty()) {
                deliverResultToReceiver(Constants.Failure_RESULT, errorMessage)
            }
            else{
                val address= addresses[0]
                val addressFragments:MutableList<String> = ArrayList()
                var add =""
                for(i in 0 .. address.maxAddressLineIndex) {
                    addressFragments.add(address.getAddressLine(i))
                    add+=address.getAddressLine(i)
                }

                deliverResultToReceiver(Constants.SUCESS_RESULT,TextUtils.join(
                        Objects.requireNonNull(System.getProperty("line.separator")).toString()
                        ,addressFragments))
            }

        }
    }

    private fun deliverResultToReceiver(resultCode: Int, addressMessage: String) {

        val bundle = Bundle()
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage)
        resultreciver!!.send(resultCode, bundle)

    }
}