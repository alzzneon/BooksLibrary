package com.project.projectuts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PeminjamanViewModel(private val repository: PeminjamanRepository) : ViewModel() {

    fun insertPeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            repository.insertPeminjaman(peminjaman)
        }
    }
}
