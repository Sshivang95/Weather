package com.shivang.weather


import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

import androidx.core.app.ActivityCompat

import com.google.android.gms.location.*



class SplashAcreen : AppCompatActivity() {

    lateinit var mfusedlocation:FusedLocationProviderClient
    private  var myRequestCode=1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_acreen)

        /*Handler(Looper.getMainLooper()).postDelayed({
                 var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)*/

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

    }
//  1.location permission --> deny
//  2.location denied through setting
//  3.GPS off
//  4.ask for permission
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
       if(CheckPermission()){
            if(LocationEnable()){
                mfusedlocation.lastLocation.addOnCompleteListener {
                    task->
                    var location:Location?=task.result
                    if(location==null){
                        NewLocation()
                    }else{
                       // Log.i("Location",location.longitude.toString())
                        window.statusBarColor= Color.parseColor("#1383C3")
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent= Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)
                    }
                }
            }else{
                Toast.makeText(this,"Please turn in your gps location", Toast.LENGTH_LONG).show()
            }
       }else{
          RequestPermission()

       }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {
       var locationRequest= com.google.android.gms.location.LocationRequest()
        locationRequest.priority=Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1

        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    private val locationCallback=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation:Location= p0.lastLocation!!
        }
    }

    private fun LocationEnable(): Boolean {
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    private fun CheckPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
          ){
            return true
           }
        return false
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==myRequestCode){
            if(grantResults.isNotEmpty() &&
                grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getLastLocation()
            }
        }
    }
}