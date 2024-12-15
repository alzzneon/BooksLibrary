package com.project.books_library.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.books_library.model.Visitors

@Dao
interface VisitorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListVisitors(visitors: List<Visitors>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitors: Visitors)

    @Query("SELECT * FROM Visitors")
    fun getAllVisitors(): LiveData<List<Visitors>>

    @Query("DELETE FROM visitors WHERE id_visitor = :id")
    suspend fun deleteById(id: Int?)

}
