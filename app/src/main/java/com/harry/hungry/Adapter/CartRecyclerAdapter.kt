package com.harry.hungry.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.hungry.R
import com.harry.hungry.database.DishEntity
import kotlinx.android.synthetic.main.recycler_cart_single_row.view.*
import org.w3c.dom.Text

class CartRecyclerAdapter(val context: Context, val dishList: List<DishEntity>) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_single_row, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val dish = dishList[position]
        holder.txtDishName.text = dish.foodName
        holder.txtDishCost.text = "Rs. "+dish.foodCost.toString()
    }

    class CartViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishCost : TextView = view.findViewById(R.id.txtDishCost)
    }
}