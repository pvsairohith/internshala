package com.example.weatherapp

import android.app.DownloadManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Math.ceil

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        window.statusBarColor= Color.parseColor("#1383C3")
        getJsonData(lat,long)
    }
    private fun getJsonData(lat: String?,long: String?)
    {
        val API_key="d782c3641dcc9cbcb8528dfb0245b3e4"
        val queue = Volley.newRequestQueue(this)
        val url="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_key}"

        val jsonrequest = JsonObjectRequest(
            Request.Method.GET, url,null,Response.Listener {
                response->

                setvalues(response)

            },Response.ErrorListener {
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonrequest)


    }

    private fun setvalues(response: JSONObject) {
        city.text=response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        coordinates.text="${lat}, ${long}"
        weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr = response.getJSONObject("main").getString("temp")
        tempr=(((tempr).toFloat()-273.15)).toInt().toString()
        temp.text="${tempr}°C"


        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        min_temp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        max_temp.text=maxtemp+"°C"

        pressure.text=response.getJSONObject("main").getString("pressure")
        humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        wind.text=response.getJSONObject("wind").getString("speed")
        degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"


    }
}
