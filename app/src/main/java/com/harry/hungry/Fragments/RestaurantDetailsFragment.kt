package com.harry.hungry.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harry.hungry.Activity.CartActivity
import com.harry.hungry.Adapter.HomeRecyclerAdapter
import com.harry.hungry.Adapter.RestaurantDetailsAdapter

import com.harry.hungry.R
import com.harry.hungry.database.DishEntity
import com.harry.hungry.model.Dish
import com.harry.hungry.util.OnItemClicked
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import kotlinx.android.synthetic.main.recycler_restaurant_detail_single_row.*

/**
 * A simple [Fragment] subclass.
 */
class RestaurantDetailsFragment(var resId : String, var resName : String) : Fragment(), OnItemClicked {
    override fun addItem() {
        btnProceedToCart.visibility = View.VISIBLE
    }

    override fun removeItem() {
        if(RestaurantDetailsAdapter.isCartEmpty)
            btnProceedToCart.visibility = View.GONE
    }

    lateinit var recyclerRestaurantDetails: RecyclerView
    lateinit var layoutManger : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : RestaurantDetailsAdapter
    lateinit var btnProceedToCart : Button
    var dishList  = listOf<DishEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_restaurant_details, container, false)
        val dishInfoList = arrayListOf<Dish>()
        btnProceedToCart = view.findViewById(R.id.btnProceedToCart)
        btnProceedToCart.visibility = View.GONE
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"+resId

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            println("response is $it")
            val data2 = it.getJSONObject("data")
            val success = data2.getBoolean("success")
            if(success) {
                val data = data2.getJSONArray("data")
                for(i in 0 until data.length()){
                    val dishJsonObject = data.getJSONObject(i)
                    val dishObject = Dish(
                        dishJsonObject.getString("id"),
                        dishJsonObject.getString("name"),
                        dishJsonObject.getString("cost_for_one"),
                        dishJsonObject.getString("restaurant_id")
                    )
                    dishInfoList.add(dishObject)
                    recyclerRestaurantDetails = view.findViewById(R.id.recyclerRestaurantDetails)
                    layoutManger = LinearLayoutManager(activity)
                    recyclerAdapter = RestaurantDetailsAdapter(activity as Context, dishInfoList,this)
                    recyclerRestaurantDetails.layoutManager = layoutManger
                    recyclerRestaurantDetails.adapter = recyclerAdapter
                }
            }
        }, Response.ErrorListener {
            Toast.makeText(activity as Context, "Volley Error Occured", Toast.LENGTH_SHORT).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "77c7528c300d27"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        btnProceedToCart.setOnClickListener {
            startActivity(Intent(activity as Context, CartActivity::class.java).putExtra("resId", resId).putExtra("resName", resName))

        }
        return view
    }

}
