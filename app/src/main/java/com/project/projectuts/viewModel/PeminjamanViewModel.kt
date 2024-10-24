package com.project.projectuts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.model.Peminjaman
import com.project.projectuts.repository.PeminjamanRepository
import kotlinx.coroutines.launch

class PeminjamanViewModel(private val repository: PeminjamanRepository) : ViewModel() {

    fun insertPeminjaman(peminjaman: Peminjaman) {
        viewModelScope.launch {
            repository.insertPeminjaman(peminjaman)
        }
    }
}
