package com.project.projectuts

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BukuDao {

    @Query("SELECT * FROM buku")
    fun getAllBuku(): LiveData<List<Buku>>

    @Insert
    suspend fun insertBuku(buku: Buku) // Fungsi untuk memasukkan buku ke dalam database

    @Update
    suspend fun updateBuku(buku: Buku)

    @Delete
    suspend fun deleteBuku(buku: Buku)
}

