package com.project.projectuts

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PengunjungDao {

    @Insert
    suspend fun insertPengunjung(pengunjung: Pengunjung)

    @Delete
    suspend fun deletePengunjung(pengunjung: Pengunjung)

    @Query("SELECT * FROM pengunjung")
    fun getAllPengunjung(): LiveData<List<Pengunjung>>

}
