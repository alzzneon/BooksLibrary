package com.project.projectuts.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.project.projectuts.model.Peminjaman

@Dao
interface PeminjamanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeminjaman(peminjaman: Peminjaman)

    @Update
    suspend fun updatePeminjaman(peminjaman: Peminjaman)

    @Delete
    suspend fun deletePeminjaman(peminjaman: Peminjaman)

    @Query("SELECT * FROM peminjam ORDER BY id ASC")
    fun getAllPeminjaman(): LiveData<List<Peminjaman>>
}
