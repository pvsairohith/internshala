package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class SplashActivity : AppCompatActivity() {
lateinit var mfusedlocation: FusedLocationProviderClient
    private var myrequestcode = 1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        val backgroundimage: ImageView = findViewById(R.id.logoimage)
        val effects = AnimationUtils.loadAnimation(this,R.anim.effects)
        backgroundimage.startAnimation(effects)



    }
    private fun getLastLocation(){
        if(checkpermission())
        {
            if(locationenable())
            {
                mfusedlocation.lastLocation.addOnCompleteListener {
                    Task->
                    val location:Location?=Task.result
                    if(location==null)
                    {
                        newlocation()
                    }
                    else
                    {
                       /* Log.i("Location",location.longitude.toString())
                        Toast.makeText(this,location.latitude.toString(),Toast.LENGTH_LONG).show()*/
                        Handler().postDelayed({
                            val intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())

                            startActivity(intent)

                            finish()
                        },5000)


                    }
                }
            }
            else
            {
                Toast.makeText(this,"please turn your gps location",Toast.LENGTH_LONG).show()
            }

        }
        else{
            RequestPermission()
        }
    }

    private fun newlocation() {

        var locationrequest = com.google.android.gms.location.LocationRequest()
        locationrequest.priority= com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationrequest.interval = 0
        locationrequest.fastestInterval = 0
        locationrequest.numUpdates =1
        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationrequest,locationCallback,Looper.myLooper())

    }
    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val lastlocation: Location = p0.lastLocation
        }
    }

    private fun locationenable(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),myrequestcode)
    }

    private fun checkpermission():Boolean{

        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||

            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ){
            return true;
        }
        return false


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==myrequestcode)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation()
            }
        }
    }

}


