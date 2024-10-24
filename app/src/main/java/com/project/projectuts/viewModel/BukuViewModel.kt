package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.model.Buku
import com.project.projectuts.repository.BukuRepository
import kotlinx.coroutines.launch

class BukuViewModel(private val bukuRepository: BukuRepository) : ViewModel() {

    val allBuku: LiveData<List<Buku>> = bukuRepository.getAllBuku()

    // Fungsi untuk menambahkan buku ke dalam database
    fun insertBuku(buku: Buku) {
        viewModelScope.launch {
            bukuRepository.insertBuku(buku)
        }
    }

    fun updateBuku(buku: Buku) {
        viewModelScope.launch {
            bukuRepository.updateBuku(buku)
        }
    }

    fun deleteBuku(buku: Buku) {
        viewModelScope.launch {
            bukuRepository.deleteBuku(buku)
        }
    }
}
