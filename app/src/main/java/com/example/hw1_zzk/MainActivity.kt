package com.example.hw1_zzk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var json : String? = null
        val items= arrayListOf<line>()
        try {
            val inputStream:InputStream = assets.open("pet_data.json")
            json = inputStream.bufferedReader().use{it.readText()}

            var jsonarr = JSONObject(json).getJSONArray("pets")
            for (i in 0..jsonarr.length()-1){
                var jsonobj = jsonarr.getJSONObject(i)
                var id: Int = jsonobj.getInt("id")
                items.add(line(jsonobj.getInt("id"), jsonobj.getString("title"), jsonobj.getString("description"), jsonobj.getString("gender"), jsonobj.getString("location"),jsonobj.getString("breed"), jsonobj.getString("age")))
            }
        }
        catch(e : IOException){
            Log.e("IO", "Json Read Failure")
        }

        val list=findViewById<RecyclerView>(R.id.list)
        val adapter=Adapter()
        list.adapter= adapter
        list.layoutManager= LinearLayoutManager(this)

        adapter.updateItems(items)

        var thisContext = this
       adapter.setItemClickListener(object:Adapter.ItemClickListener{
            override fun onItemClick(position: Int) {
                var no: Int = position + 1
                Log.d("@=>","onClick: ITEM $no")

                val intent = Intent(thisContext, details::class.java)
                intent.putExtra("No", no)
                startActivity(intent)
            }
        })
    }
}

class TextViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.line, parent, false)
) {
    fun update(item: line) {
        itemView.findViewById<TextView>(R.id.title).setText(item.getTitle())
        itemView.findViewById<TextView>(R.id.text_location).setText("Location: "+item.getLocation())
        itemView.findViewById<TextView>(R.id.text_age).setText("Age: " + item.getAge())
        itemView.findViewById<TextView>(R.id.text_breed).setText("Breed: " + item.getBreed())
        itemView.findViewById<ImageView>(R.id.description_image).setImageResource(R.drawable.img)
    }
}

class Adapter:RecyclerView.Adapter<TextViewHolder>(){
    private val items = arrayListOf<line>()
    private var itemClickListener: ItemClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return  TextViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.update(items.get(position))
        holder.itemView.setOnClickListener{
            itemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(Items:List<line>){
        this.items.clear()
        this.items.addAll(Items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    interface ItemClickListener{
        fun onItemClick(position: Int)
    }
}


