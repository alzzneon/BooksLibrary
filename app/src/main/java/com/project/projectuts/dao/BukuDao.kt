package com.project.projectuts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.projectuts.model.Buku

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

