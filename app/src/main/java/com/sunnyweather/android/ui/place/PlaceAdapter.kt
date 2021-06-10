package com.sunnyweather.android.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter (private val fragment: PlaceFragment, private val placeList: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val viewHolder = ViewHolder(view)
        Log.d("**PlaceAdapter**", "onCreateViewHolder ${fragment.activity.toString()}")

        // 点击事件
        viewHolder.itemView.setOnClickListener {
            val activity = fragment.activity
            val place = placeList[viewHolder.adapterPosition]
            val lng = place.location.lng
            val lat = place.location.lat

            // 根据fragment在不同的activity上做不同的处理
            if (activity is WeatherActivity) {
                // 此时在WeatherActivity，隐藏navigation bar，刷新天气
                activity.binding.drawerLayout.closeDrawers()
                Log.d("**PlaceAdapter**", "close drawer")
                activity.viewModel.locationLng = lng
                activity.viewModel.locationLat = lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {
                // 此时在MainActivity，开启WeatherActivity
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", lng)
                    putExtra("location_lat", lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.viewModel.savePlace(place)  // 存储place
        }
        return viewHolder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
}