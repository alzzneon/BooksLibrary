package com.project.projectuts

import androidx.lifecycle.LiveData

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
