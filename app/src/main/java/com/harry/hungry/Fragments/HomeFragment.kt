package com.harry.hungry.Fragments


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harry.hungry.Activity.RestaurantDetailsActivity
import com.harry.hungry.Adapter.HomeRecyclerAdapter
import com.harry.hungry.R
import com.harry.hungry.database.RestaurantEntity
import com.harry.hungry.model.Restaurant
import com.harry.hungry.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class HomeFragment : Fragment() {
    lateinit var recyclerHome : RecyclerView
    lateinit var layoutManger : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : HomeRecyclerAdapter
    var restaurantInfoList = arrayListOf<Restaurant>()
    var ratingComparator = Comparator<Restaurant>{res1, res2->
        if(res1.restaurantRating.compareTo(res2.restaurantRating, true )== 0){
            res1.restaurantName.compareTo(res2.restaurantName, true)
        }
        else {
            res1.restaurantRating.compareTo(res2.restaurantRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try{
                    println("response is $it")
                    val data2 = it.getJSONObject("data")
                    val success = data2.getBoolean("success")
                    if(success){
                        val data = data2.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val restaurantJsonObject = data.getJSONObject(i)
                            val restaurantObject = Restaurant(
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                restaurantJsonObject.getString("cost_for_one"),
                                restaurantJsonObject.getString("image_url")
                            )
                            restaurantInfoList.add(restaurantObject)
                            recyclerHome = view.findViewById(R.id.recyclerHome)
                            layoutManger = LinearLayoutManager(activity)
                            recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantInfoList)
                            recyclerHome.adapter = recyclerAdapter
                            recyclerHome.layoutManager = layoutManger
                        }
                    }
                    else{
                        Toast.makeText(activity as Context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }
                } catch (e : JSONException){
                    Toast.makeText(activity as Context, "Some unexpected error occurred", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(activity as Context, "Volley Error Occurred", Toast.LENGTH_SHORT).show()
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
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){
                text, listener->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    activity?.finish()
            }
            dialog.setNegativeButton("Exit"){
                text, listener->
                    ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(restaurantInfoList, ratingComparator)
            restaurantInfoList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}
