package com.harry.hungry.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.harry.hungry.Adapter.CartRecyclerAdapter
import com.harry.hungry.R
import com.harry.hungry.database.DishDatabase
import com.harry.hungry.database.DishEntity
import org.apache.http.params.CoreConnectionPNames
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method

class CartActivity : AppCompatActivity() {
    lateinit var txtOrderingFrom : TextView
    lateinit var btnPlaceOrder : Button
    lateinit var txtRestaurantName : TextView
    lateinit var recyclerCart : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var sharedPreferences: SharedPreferences
    var dishList = listOf<DishEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        txtOrderingFrom = findViewById(R.id.txtOrderingFrom)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        txtRestaurantName = findViewById(R.id.txtRestaurantName)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar("My Cart")
        sharedPreferences = getSharedPreferences("Hungry Preferences", Context.MODE_PRIVATE)
        var resID : String = ""
        var resName : String = ""
        var userId : String? = sharedPreferences.getString("user_id", "")
        if(intent != null){
            resID = intent.getStringExtra("resId")
            resName = intent.getStringExtra("resName")
        }
        txtRestaurantName.text = resName
        dishList = RetrieveOrder(this@CartActivity as Context).execute().get()
        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this@CartActivity)
        recyclerAdapter = CartRecyclerAdapter(this@CartActivity, dishList)
        var sum : Int = 0
        for (i in 0 until dishList.size)
            sum = sum + dishList[i].foodCost
        recyclerCart.layoutManager = layoutManager
        recyclerCart.adapter = recyclerAdapter
        btnPlaceOrder.text = "Place Order (Total Rs. "+(sum.toString())+")"
        btnPlaceOrder.setOnClickListener {
            val queue = Volley.newRequestQueue(this@CartActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("user_id", userId)
            jsonParams.put("restaurant_id", resID)
            jsonParams.put("total_cost", sum.toString())
            val foodArray = JSONArray()
            for(i in 0 until dishList.size){
                val foodId = JSONObject()
                foodId.put("food_item_id", dishList[i].foodId.toString())
                foodArray.put(i, foodId)
            }
            jsonParams.put("food", foodArray)
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,

                Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success){
                        startActivity(Intent(this@CartActivity, OrderPlaced::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this@CartActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@CartActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "77c7528c300d27"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
    }
    fun setUpToolbar(resName : String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = resName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    class RetrieveOrder(val context: Context) : AsyncTask<Void, Void, List<DishEntity>>(){
        override fun doInBackground(vararg params: Void?): List<DishEntity> {
            val db = Room.databaseBuilder(context, DishDatabase::class.java, "dishes-db").build()
            return db.dishDao().getAllDish()
        }

    }
}
