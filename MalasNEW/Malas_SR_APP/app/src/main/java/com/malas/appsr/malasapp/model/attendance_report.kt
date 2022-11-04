package com.malas.appsr.malasapp.model

class attendance_report() {
    var name:String?=null

    var timein: String? = null
    var location_lat_in: String?= null
    var location_lng_in: String? = null
    var time_in_img: String? = null

    var timeout: String? = null
    var location_lat_out: String? = null
    var location_lng_out: String? = null
    var time_out_img: String? = null

    constructor(name:String?,timein: String?, location_lat_in: String?, location_lng_in: String?, time_in_img: String?, timeout: String?, location_lat_out: String?, location_lng_out: String?, time_out_img: String?) : this() {

        this.name=name

        this.timein = timein
        this.location_lat_in = location_lat_in
        this.location_lng_in = location_lng_in
        this.time_in_img = time_in_img

        this.timeout = timeout
        this.location_lat_out = location_lat_out
        this.location_lng_out =location_lng_out
        this.time_out_img=time_out_img

    }

    fun getname(): String? {
        return this.name
    }

    fun setname(name: String) {
        this.name = name
    }

    fun gettimein(): String? {
        return this.timein
    }

    fun settimein(timein: String?) {
        this.timein = timein
    }

    fun getlocation_lat_in(): String? {
        return this.location_lat_in
    }

    fun setlocation_lat_in(location_lat_in: String?) {
        this.location_lat_in = location_lat_in
    }

    fun getlocation_lng_in(): String? {
        return this.location_lng_in
    }

    fun setlocation_lng_in(location_lng_in: String?) {
        this.location_lng_in = location_lng_in
    }

    fun gettime_in_img(): String? {
        return this.time_in_img
    }

    fun settime_in_img(time_in_img: String?) {
        this.time_in_img = time_in_img
    }

    fun gettimeout(): String? {
        return this.timeout
    }

    fun settimeout(timeout: String?) {
        this.timeout = timeout
    }

    fun getlocation_lat_out(): String? {
        return this.location_lat_out
    }

    fun setlocation_lat_out(location_lat_out: String?) {
        this.location_lat_out = location_lat_out
    }

    fun getlocation_lng_out(): String? {
        return this.location_lng_out
    }

    fun setlocation_lng_out(location_lng_out: String?) {
        this.location_lng_out = location_lng_out
    }


    fun gettime_out_img(): String? {
        return this.time_out_img
    }

    fun settime_out_img(time_out_img: String?) {
        this.time_out_img = time_out_img
    }

}