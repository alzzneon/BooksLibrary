package com.project.projectuts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.dao.PeminjamanDao
import com.project.projectuts.model.Peminjaman
import kotlinx.coroutines.launch

class PeminjamanViewModel(private val peminjamanDao: PeminjamanDao) : ViewModel() {

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    val allPeminjaman: LiveData<List<Peminjaman>> = peminjamanDao.getAllPeminjaman() // Pastikan Anda memiliki ini

    fun addPeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            peminjamanDao.insertPeminjaman(peminjaman)
            _statusMessage.value = "Peminjaman berhasil ditambahkan"
        }
    }

    fun deletePeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            peminjamanDao.deletePeminjaman(peminjaman)
        }
    }
}
