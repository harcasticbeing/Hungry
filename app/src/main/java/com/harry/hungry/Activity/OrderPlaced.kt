package com.harry.hungry.Activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.harry.hungry.R
import com.harry.hungry.database.DishDatabase

class OrderPlaced : AppCompatActivity() {
    lateinit var btnOrderPlaced : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        btnOrderPlaced = findViewById(R.id.btnOrderPlaced)
        btnOrderPlaced.setOnClickListener {
            val clrCart = RestaurantDetailsActivity.ClearDBAsyncTask(applicationContext).execute().get()
            startActivity(Intent(this@OrderPlaced, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }
    }

    override fun onBackPressed() {
        val clrCart = RestaurantDetailsActivity.ClearDBAsyncTask(applicationContext).execute().get()
        startActivity(Intent(this@OrderPlaced, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
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
