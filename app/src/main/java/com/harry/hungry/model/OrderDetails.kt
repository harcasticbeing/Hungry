package com.harry.hungry.model

import org.json.JSONArray

data class OrderDetails(
    val orderId : String,
    val resName : String,
    val orderPlacedAt : String,
    val foodItems : ArrayList<Dish>
)