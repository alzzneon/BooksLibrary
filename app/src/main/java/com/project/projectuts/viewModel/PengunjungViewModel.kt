package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.model.Pengunjung
import com.project.projectuts.repository.PengunjungRepository
import kotlinx.coroutines.launch

class PengunjungViewModel(private val repository: PengunjungRepository) : ViewModel() {

    // LiveData untuk mendapatkan semua pengunjung
    val allPengunjung: LiveData<List<Pengunjung>> = repository.getAllPengunjung()

    fun insertPengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            repository.insertPengunjung(pengunjung)
        }
    }

    fun deletePengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            repository.deletePengunjung(pengunjung)
        }
    }
}
