package com.project.projectuts.repository

import androidx.lifecycle.LiveData
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.model.Peminjaman

class PeminjamanRepository(private val peminjamanDao: PeminjamanDao) {

    fun getAllPeminjam(): LiveData<List<Peminjaman>> {
        return peminjamanDao.getAllPeminjaman()
    }

    suspend fun insertPeminjam(peminjaman: Peminjaman) {
        peminjamanDao.insertPeminjaman(peminjaman)
    }

    suspend fun updatePeminjam(peminjaman: Peminjaman) {
        peminjamanDao.updatePeminjaman(peminjaman)
    }

    suspend fun deletePeminjam(peminjaman: Peminjaman) {
        peminjamanDao.deletePeminjaman(peminjaman)
    }
}
