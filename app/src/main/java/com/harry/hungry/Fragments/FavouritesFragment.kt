package com.harry.hungry.Fragments


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.harry.hungry.Adapter.FavouriteRecyclerAdapter
import com.harry.hungry.R
import com.harry.hungry.database.RestaurantDatabase
import com.harry.hungry.database.RestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourite : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    var dbRestaurantList = listOf<RestaurantEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        layoutManager = LinearLayoutManager(activity)
        dbRestaurantList = RetrieveFavourites(activity as Context).execute().get()
        if(activity != null){
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbRestaurantList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>(){
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()
            return db.restaurantDao().getAllRestaurants()
        }

    }
}
