package com.harry.hungry.Fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.harry.hungry.Adapter.OrderHistoryRecyclerAdapter
import com.harry.hungry.R
import com.harry.hungry.model.Dish
import com.harry.hungry.model.OrderDetails

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {
    lateinit var recyclerOrderHistory : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryRecyclerAdapter
    var orderHistoryList = arrayListOf<OrderDetails>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        var sharedPreferences: SharedPreferences? = activity?.getSharedPreferences("Hungry Preferences", Context.MODE_PRIVATE)
        var userId = sharedPreferences?.getString("user_id", "")
        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory)
        layoutManager = LinearLayoutManager(activity as Context)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/" + userId
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                println("response is $it")
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success){
                    val resArray = data.getJSONArray("data")
                    for(i in 0 until resArray.length()){
                        val foodItems = ArrayList<Dish>()
                        val orderObject = resArray.getJSONObject(i)
                        val foodItems2 = orderObject.getJSONArray("food_items")
                        for(j in 0 until foodItems2.length()){
                            val foodItemObject = foodItems2.getJSONObject(j)
                            val dish = Dish(
                                foodItemObject.getString("food_item_id"),
                                foodItemObject.getString("name"),
                                foodItemObject.getString("cost"),
                                "10"
                            )
                            foodItems.add(dish)
                        }
                        val orderDetails = OrderDetails(
                            orderObject.getString("order_id"),
                            orderObject.getString("restaurant_name"),
                            orderObject.getString("order_placed_at"),
                            foodItems
                        )
                        orderHistoryList.add(orderDetails)
                        if(activity != null){
                            recyclerAdapter = OrderHistoryRecyclerAdapter(activity as Context, orderHistoryList)
                            val mLayoutManager = LinearLayoutManager(activity as Context)
                            recyclerOrderHistory.layoutManager = mLayoutManager
                            recyclerOrderHistory.adapter = recyclerAdapter
                        }
                    }
                }
                else{
                    Toast.makeText(activity as Context, "Some error Occurred", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity as Context, "Some error Occurred", Toast.LENGTH_SHORT).show()
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "77c7528c300d27"
                return headers
            }
        }
        queue.add(jsonObjectRequest)

        return view
    }


}
