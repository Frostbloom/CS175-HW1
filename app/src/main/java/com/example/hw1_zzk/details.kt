package com.example.hw1_zzk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        var id:Int = intent.extras!!.getInt("No")
        var json : String? = null
        try {
            val inputStream: InputStream = assets.open("pet_data.json")
            json = inputStream.bufferedReader().use{it.readText()}

            var jsonarr = JSONObject(json).getJSONArray("pets")

            var jsonobj = jsonarr.getJSONObject(id - 1)
            findViewById<TextView>(R.id.detail_title).setText(jsonobj.getString("title"))
            findViewById<TextView>(R.id.detail_discription).setText((jsonobj.getString("description")))
            findViewById<ImageView>(R.id.detail_image).setImageResource(R.drawable.img)
        } catch(e : IOException){
            Log.e("IO", "Json Read Failure")
        }
    }
}