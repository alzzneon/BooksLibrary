package com.project.projectuts.repository

import androidx.lifecycle.LiveData
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.model.Peminjaman

class PeminjamanRepository(private val peminjamanDao: PeminjamanDao) {

    // Mengambil semua data peminjam
    fun getAllPeminjam(): LiveData<List<Peminjaman>> {
        return peminjamanDao.getAllPeminjaman()
    }

    // Menambahkan peminjam
    suspend fun insertPeminjam(peminjaman: Peminjaman) {
        peminjamanDao.insertPeminjaman(peminjaman)
    }

    // Mengupdate peminjam
    suspend fun updatePeminjam(peminjaman: Peminjaman) {
        peminjamanDao.updatePeminjaman(peminjaman) // Pastikan ini sesuai dengan metode yang ada di PeminjamanDao
    }

    // Menghapus peminjam
    suspend fun deletePeminjam(peminjaman: Peminjaman) {
        peminjamanDao.deletePeminjaman(peminjaman) // Pastikan ini sesuai dengan metode yang ada di PeminjamanDao
    }
}
