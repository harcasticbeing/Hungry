package com.harry.hungry.Adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.harry.hungry.R
import com.harry.hungry.database.DishDatabase
import com.harry.hungry.database.DishEntity
import com.harry.hungry.model.Dish
import com.harry.hungry.util.OnItemClicked

class RestaurantDetailsAdapter(val context : Context, val itemList : ArrayList<Dish>, val listener : OnItemClicked) : RecyclerView.Adapter<RestaurantDetailsAdapter.RestaurantDetailsViewHolder>(){
    companion object{
        var isCartEmpty = true
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_restaurant_detail_single_row, parent, false)
        return RestaurantDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantDetailsViewHolder, position: Int) {
        val dish = itemList[position]
        val dishEntity = DishEntity(dish.dishId.toInt(), dish.dishCost.toInt(), dish.dishName)
        holder.txtSerial.text = (position+1).toString()
        holder.txtDishCost.text = "Rs. "+dish.dishCost
        holder.txtDishName.text = dish.dishName
        holder.btnRemoveFromCart.visibility = View.GONE
        holder.btnAddToCart.setOnClickListener {
            val async = DBAsyncTask(context, dishEntity, 1).execute()
            val result = async.get()
            if(result){
                holder.btnAddToCart.visibility = View.GONE
                holder.btnRemoveFromCart.visibility = View.VISIBLE
                isCartEmpty = false
                listener.addItem()

            }
            else{
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        }
        holder.btnRemoveFromCart.setOnClickListener {
            val async = DBAsyncTask(context, dishEntity, 2).execute()
            val result = async.get()
            if(result){
                holder.btnRemoveFromCart.visibility = View.GONE
                holder.btnAddToCart.visibility = View.VISIBLE
                val async2 = DBAsyncTask(context, dishEntity, 3).execute().get()
                if(async2)
                    if(isCartEmpty){
                        listener.removeItem()
                    }
            }
            else{
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
            holder.btnRemoveFromCart.visibility = View.GONE
            holder.btnAddToCart.visibility = View.VISIBLE
        }
    }

    class RestaurantDetailsViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val txtSerial : TextView = view.findViewById(R.id.txtSerial)
        val txtDishName : TextView = view.findViewById(R.id.txtDishName)
        val txtDishCost : TextView = view.findViewById(R.id.txtDishCost)
        val btnAddToCart : Button = view.findViewById(R.id.btnAddToCart)
        val btnRemoveFromCart : Button = view.findViewById(R.id.btnRemoveFromCart)
        //val btnProceedToCart : Button = view.findViewById(R.id.btnProceedToCart)
    }
    class DBAsyncTask (val context: Context, val dishEntity: DishEntity, val mode : Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dishes-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.dishDao().insertDish(dishEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.dishDao().removeDish(dishEntity)
                    db.close()
                    return true
                }
                3->{
                    val x =db.dishDao().getSize()
                    if(x == 0)
                        isCartEmpty = true
                    println(x)
                    println(isCartEmpty)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}