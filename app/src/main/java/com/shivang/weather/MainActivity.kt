package com.shivang.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")
       // Toast.makeText(this, lat+","+long, Toast.LENGTH_SHORT).show()
       //tochange the color of statusBar
        window.statusBarColor=Color.parseColor("#1383C3")
        getJsonData(lat,long)
    }

    private fun getJsonData(lat: String?, long: String?) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=6c8be4fbe8d09a81970216047a1e10ce"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response) },
            Response.ErrorListener {
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject) {
            city.text=response.getString("name")
            var lat=response.getJSONObject("coord").getString("lat")
            var long=response.getJSONObject("coord").getString("lon")
            coordinates.text="${lat},${long}"
            weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")


            var tempMin=response.getJSONObject("main").getString("temp")
            tempMin=((((tempMin).toFloat()-273.15)).toInt()).toString()
            min_temp.text= "$tempMin°C"
            var tempMax=response.getJSONObject("main").getString("temp")
            tempMax=((ceil((tempMax).toFloat()-273.15)).toInt()).toString()
            max_temp.text= "$tempMax°C"

            pressure.text=response.getJSONObject("main").getString("pressure")
            humidity.text=response.getJSONObject("main").getString("humidity")+"%"
            wind.text=response.getJSONObject("wind").getString("speed")
            degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"
            //gust.text="Gust : "+response.getJSONObject("wind").getString("gus")+"°"
    }
}