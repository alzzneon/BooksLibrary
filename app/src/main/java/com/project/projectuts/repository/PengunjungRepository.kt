package com.project.projectuts.repository

import androidx.lifecycle.LiveData
import com.project.projectuts.model.Pengunjung
import com.project.projectuts.dao.PengunjungDao

class PengunjungRepository(private val pengunjungDao: PengunjungDao) {

    // Mengembalikan LiveData dari daftar pengunjung
    fun getAllPengunjung(): LiveData<List<Pengunjung>> {
        return pengunjungDao.getAllPengunjung()
    }

    // Fungsi untuk menambahkan pengunjung
    suspend fun insertPengunjung(pengunjung: Pengunjung) {
        pengunjungDao.insertPengunjung(pengunjung)
    }

    suspend fun deletePengunjung(pengunjung: Pengunjung) {
        pengunjungDao.deletePengunjung(pengunjung)
    }
}
