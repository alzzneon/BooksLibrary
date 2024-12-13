package com.project.BooksLibrary.Dao

import androidx.room.*
import com.project.BooksLibrary.Model.Visitors

@Dao
interface VisitorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListVisitors(visitors: List<Visitors>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitors: Visitors)

    @Query("SELECT * FROM Visitors")
    suspend fun getAllVisitors(): List<Visitors>

    @Query("DELETE FROM visitors WHERE id_visitors = :id")
    suspend fun deleteById(id: Int?)
}
