package com.example.simpleshoppingrework.db

import androidx.room.*
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingList)

    @Query("SELECT * FROM shoppinglists")
    fun readData(): Flow<List<ShoppingList>>

    @Delete
    suspend fun deleteLists(lists: List<ShoppingList>)
}