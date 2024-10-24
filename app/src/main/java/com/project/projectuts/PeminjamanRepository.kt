package com.project.projectuts

class PeminjamanRepository(private val peminjamanDao: PeminjamanDao) {

    suspend fun getAllPeminjamanWithDetails(): List<PeminjamanRelasi> {
        return peminjamanDao.getAllPeminjamanWithDetails()
    }

    suspend fun insertPeminjaman(peminjaman: Peminjaman) {
        peminjamanDao.insertPeminjaman(peminjaman)
    }
}
