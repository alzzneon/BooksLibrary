package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.model.Buku
import com.project.projectuts.dao.BukuDao // Pastikan DAO diimpor
import kotlinx.coroutines.launch

class BukuViewModel(private val bukuDao: BukuDao) : ViewModel() {

    val allBuku: LiveData<List<Buku>> = bukuDao.getAllBuku()

    fun insertBuku(buku: Buku) {
        viewModelScope.launch {
            bukuDao.insertBuku(buku)
        }
    }

    fun updateBuku(buku: Buku) {
        viewModelScope.launch {
            bukuDao.updateBuku(buku)
        }
    }

    fun deleteBuku(buku: Buku) {
        viewModelScope.launch {
            bukuDao.deleteBuku(buku)
        }
    }
}
