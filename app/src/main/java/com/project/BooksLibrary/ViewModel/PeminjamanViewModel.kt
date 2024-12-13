package com.project.BooksLibrary.ViewModel

import androidx.lifecycle.*
import com.project.BooksLibrary.Dao.PeminjamanDao
import com.project.BooksLibrary.Model.Peminjaman
import kotlinx.coroutines.launch

class PeminjamanViewModel(private val peminjamanDao: PeminjamanDao) : ViewModel() {

    val allPeminjaman: LiveData<List<Peminjaman>> = peminjamanDao.getAllPeminjaman()
    val statusMessage: MutableLiveData<String> = MutableLiveData()

    fun addPeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            peminjamanDao.insertPeminjaman(peminjaman)
            statusMessage.value = "Peminjaman berhasil ditambahkan"
        }
    }

    fun deletePeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            peminjamanDao.deletePeminjaman(peminjaman)
            statusMessage.value = "Peminjaman berhasil dihapus"
        }
    }
}
