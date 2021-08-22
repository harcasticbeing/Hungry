package com.harry.hungry.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harry.hungry.R
import com.harry.hungry.model.OrderDetails
import kotlinx.android.synthetic.main.recycler_order_history_single_row.view.*

class OrderHistoryRecyclerAdapter(val context: Context, val orderHistoryList : ArrayList<OrderDetails>) : RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_single_row, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orderHistoryList[position]
        holder.txtRestaurantName.text = order.resName
        holder.txtOrderData.text = order.orderPlacedAt
        val childLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(holder.recyclerChild.context)
        val recyclerAdapter : OrderChildAdapter = OrderChildAdapter(holder.recyclerChild.context, order.foodItems)
        holder.recyclerChild.layoutManager = childLayoutManager
        holder.recyclerChild.adapter = recyclerAdapter
    }

    class OrderHistoryViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val txtRestaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val txtOrderData : TextView = view.findViewById(R.id.txtOrderData)
        val recyclerChild : RecyclerView =view.findViewById(R.id.recyclerChild)
    }
}