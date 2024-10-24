package com.project.projectuts

import androidx.lifecycle.LiveData

class BukuRepository(private val bukuDao: BukuDao) {

    // Fungsi untuk mengambil semua buku
    fun getAllBuku(): LiveData<List<Buku>> {
        return bukuDao.getAllBuku()
    }

    // Fungsi untuk menambahkan buku ke dalam database
    suspend fun insertBuku(buku: Buku) {
        bukuDao.insertBuku(buku)
    }

    suspend fun updateBuku(buku: Buku) {
        bukuDao.updateBuku(buku)
    }

    suspend fun deleteBuku(buku: Buku) {
        bukuDao.deleteBuku(buku)
    }
}
