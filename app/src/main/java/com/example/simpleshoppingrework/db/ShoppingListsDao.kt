package com.example.simpleshoppingrework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingList)

    @Query("SELECT * FROM shoppinglists")
    fun readData(): Flow<List<ShoppingList>>
}