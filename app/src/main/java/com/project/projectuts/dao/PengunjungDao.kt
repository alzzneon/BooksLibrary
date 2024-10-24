package com.project.projectuts.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.projectuts.model.Pengunjung

@Dao
interface PengunjungDao {

    @Insert
    suspend fun insertPengunjung(pengunjung: Pengunjung)

    @Delete
    suspend fun deletePengunjung(pengunjung: Pengunjung)

    @Query("SELECT * FROM pengunjung")
    fun getAllPengunjung(): LiveData<List<Pengunjung>>

}
