package com.project.projectuts.repository

import androidx.lifecycle.LiveData
import com.project.projectuts.model.Buku
import com.project.projectuts.dao.BukuDao

class BukuRepository(private val bukuDao: BukuDao) {

    fun getAllBuku(): LiveData<List<Buku>> {
        return bukuDao.getAllBuku()
    }

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
