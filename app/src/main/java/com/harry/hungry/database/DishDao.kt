package com.harry.hungry.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DishDao {
    @Insert
    fun insertDish(dishEntity: DishEntity)
    @Delete
    fun removeDish(dishEntity: DishEntity)
    @Query("SELECT * FROM dishes")
    fun getAllDish() : List<DishEntity>
    @Query("SELECT COUNT(*) FROM dishes")
    fun getSize() : Int
    @Query("DELETE FROM dishes")
    fun emptyCart()
}
