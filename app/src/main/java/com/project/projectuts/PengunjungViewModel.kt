package com.project.projectuts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
