package com.project.projectuts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.projectuts.model.Buku
import com.project.projectuts.model.Pengunjung

@Dao
interface PengunjungDao {

    @Insert
    suspend fun insertPengunjung(pengunjung: Pengunjung)

    @Update
    suspend fun updatePengunjung(pengunjung: Pengunjung)

    @Delete
    suspend fun deletePengunjung(pengunjung: Pengunjung)

    @Query("SELECT * FROM pengunjung")
    fun getAllPengunjung(): LiveData<List<Pengunjung>>

    @Query("SELECT * FROM pengunjung WHERE id = :id")
    suspend fun getPengunjungById(id: Int): Pengunjung?

}
