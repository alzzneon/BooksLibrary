package com.project.BooksLibrary.dao

import androidx.room.*
import com.project.BooksLibrary.model.Visitors

@Dao
interface VisitorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitors(visitors: List<Visitors>)

    @Query("SELECT * FROM Visitors")
    fun getAllVisitors(): List<Visitors>

    @Query("SELECT * FROM visitors WHERE id_visitors = :id")
    suspend fun getVisitorById(id: Int): Visitors?

}
