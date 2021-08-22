package com.harry.hungry.Adapter

import android.content.Context
import android.nfc.tech.TagTechnology
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.hungry.R
import com.harry.hungry.model.Dish
import kotlinx.android.synthetic.main.recycler_order_history_food_single_row.view.*

class OrderChildAdapter(val context: Context, val itemList : ArrayList<Dish>) : RecyclerView.Adapter<OrderChildAdapter.OrderChildAdapterViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderChildAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_food_single_row, parent, false)
        return OrderChildAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OrderChildAdapterViewHolder, position: Int) {
        holder.txtDishName.text = itemList[position].dishName
        holder.txtDishCost.text = itemList[position].dishCost
    }

    class OrderChildAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishCost : TextView = view.findViewById(R.id.txtDishCost)
    }

}