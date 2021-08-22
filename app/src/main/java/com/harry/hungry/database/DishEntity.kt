package com.harry.hungry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey @ColumnInfo(name = "food_id")val foodId : Int,
    @ColumnInfo(name = "food_cost")val foodCost : Int,
    @ColumnInfo(name = "food_name")val foodName : String
)