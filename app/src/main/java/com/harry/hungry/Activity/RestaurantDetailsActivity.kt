package com.harry.hungry.Activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.harry.hungry.Adapter.RestaurantDetailsAdapter
import com.harry.hungry.Fragments.RestaurantDetailsFragment
import com.harry.hungry.R
import com.harry.hungry.database.DishDatabase
import com.harry.hungry.database.RestaurantDatabase
import com.harry.hungry.database.RestaurantEntity
import com.harry.hungry.util.OnItemClicked

class RestaurantDetailsActivity : AppCompatActivity() {
    lateinit var frameLayout: FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var rlContent : RelativeLayout
    lateinit var btnAddToFavourites : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        rlContent = findViewById(R.id.rlContent)
        frameLayout = findViewById(R.id.frame2)
        toolbar = findViewById(R.id.toolbar)
        btnAddToFavourites = findViewById(R.id.btnAddToFavoutrites)
        var resId : String = ""
        var resName : String = ""
        var resRating : String = ""
        var costForOne : String = ""
        var resImage : String = ""
        if(intent != null){
            resId = intent.getStringExtra("resId")
            resName = intent.getStringExtra("resName")
            resRating = intent.getStringExtra("resRating")
            costForOne = intent.getStringExtra("costForOne")
            resImage = intent.getStringExtra("resImage")
        }

        setUpToolbar(resName)
        supportFragmentManager.beginTransaction().replace(R.id.frame2, RestaurantDetailsFragment(resId, resName)).commit()
        val restaurantEntity = RestaurantEntity(
            resId.toInt() as Int,
            resName,
            resRating,
            costForOne,
            resImage
        )
        val checkFav = DBAsyncTask(applicationContext, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav){
            btnAddToFavourites.text = "Remove from Favourites"
            val favColor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
            btnAddToFavourites.setBackgroundColor(favColor)
        }
        btnAddToFavourites.setOnClickListener {
            if(!DBAsyncTask(applicationContext, restaurantEntity, 1).execute().get()){
                val async = DBAsyncTask(applicationContext, restaurantEntity, 2).execute()
                val result = async.get()
                if(result){
                    Toast.makeText(this@RestaurantDetailsActivity, "Restaurant added to Favourites", Toast.LENGTH_SHORT).show()
                    btnAddToFavourites.text = "Remove from Favourites"
                    val favColor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                    btnAddToFavourites.setBackgroundColor(favColor)
                }
                else{
                    Toast.makeText(this@RestaurantDetailsActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val async = DBAsyncTask(applicationContext, restaurantEntity, 3).execute()
                val result = async.get()
                if(result){
                    Toast.makeText(this@RestaurantDetailsActivity, "Restaurant removed from Favourites", Toast.LENGTH_SHORT).show()
                    btnAddToFavourites.text = "Add to Favourites"
                    val favColor = ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark)
                    btnAddToFavourites.setBackgroundColor(favColor)
                }
                else{
                    Toast.makeText(this@RestaurantDetailsActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
    fun setUpToolbar(resName : String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = resName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        val clrCart = ClearDBAsyncTask(applicationContext).execute().get()
        onBackPressed()
        return true;
    }

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode : Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1-> {
                    val restaurant : RestaurantEntity = db.restaurantDao().getRestaurantById(restaurantEntity.resId.toString())
                    db.close()
                    return restaurant != null
                }

                2-> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

                3-> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    override fun onBackPressed() {
        val clrCart = ClearDBAsyncTask(applicationContext).execute().get()
        super.onBackPressed()
    }
    class ClearDBAsyncTask(val context: Context) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dishes-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.dishDao().emptyCart()
            db.close()
            return true
        }

    }
}
