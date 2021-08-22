package com.harry.hungry.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.hungry.Activity.RestaurantDetailsActivity
import com.harry.hungry.R
import com.harry.hungry.database.RestaurantEntity
import com.harry.hungry.model.Restaurant
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter (val context: Context, val restaurantList : List<RestaurantEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.txtRestaurantName.text = restaurant.resName
        holder.txtRestaurantPrice.text = "Rs. "+restaurant.costForOne
        holder.txtRestaurantRating.text = restaurant.resRating
        Picasso.get().load(restaurant.resImage).error(R.mipmap.ic_launcher).into(holder.imgRestaurantImage)

        holder.llContent.setOnClickListener{
            context.startActivity(
                Intent(context, RestaurantDetailsActivity::class.java).putExtra("resId", restaurant.resId.toString())
                    .putExtra("resName", restaurant.resName)
                    .putExtra("resRating", restaurant.resRating)
                    .putExtra("costForOne", restaurant.costForOne)
                    .putExtra("resImage", restaurant.resImage))
        }
    }

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPrice : TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRestaurantRating : TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }
}