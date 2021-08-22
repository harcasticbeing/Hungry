package com.harry.hungry.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.harry.hungry.Activity.RestaurantDetailsActivity
import com.harry.hungry.Fragments.ProfileFragment
import com.harry.hungry.Fragments.RestaurantDetailsFragment
import com.harry.hungry.R
import com.harry.hungry.model.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_single_row.view.*

class HomeRecyclerAdapter (val context: Context, val itemList: ArrayList<Restaurant>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtRestaurantName.text = restaurant.restaurantName
        holder.txtRestaurantPrice.text = "Rs. "+restaurant.restaurantPrice
        holder.txtRestaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.mipmap.ic_launcher).into(holder.imgRestaurantImage)

        holder.llContent.setOnClickListener{
            context.startActivity(Intent(context, RestaurantDetailsActivity::class.java).putExtra("resId", restaurant.restaurantId)
                .putExtra("resName", restaurant.restaurantName)
                .putExtra("resRating", restaurant.restaurantRating)
                .putExtra("costForOne", restaurant.restaurantPrice)
                .putExtra("resImage", restaurant.restaurantImage))
        }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPrice : TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRestaurantRating : TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }
}