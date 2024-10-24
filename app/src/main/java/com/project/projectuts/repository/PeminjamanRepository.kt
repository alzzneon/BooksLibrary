package com.project.projectuts.repository

import com.project.projectuts.model.Peminjaman
import com.project.projectuts.model.PeminjamanRelasi
import com.project.projectuts.dao.PeminjamanDao

class PeminjamanRepository(private val peminjamanDao: PeminjamanDao) {

    suspend fun getAllPeminjamanWithDetails(): List<PeminjamanRelasi> {
        return peminjamanDao.getAllPeminjamanWithDetails()
    }

    suspend fun insertPeminjaman(peminjaman: Peminjaman) {
        peminjamanDao.insertPeminjaman(peminjaman)
    }
}
