package com.project.books_library.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.books_library.model.Lending

@Dao
interface LendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLending(lending: Lending)

    @Query("SELECT * FROM lending")
    fun getAllLendings(): LiveData<List<Lending>>

    @Update
    suspend fun updateLending(lending: Lending)

    @Query("DELETE FROM lending WHERE id_lending = :lendingId")
    suspend fun deleteLendingById(lendingId: Int)

}