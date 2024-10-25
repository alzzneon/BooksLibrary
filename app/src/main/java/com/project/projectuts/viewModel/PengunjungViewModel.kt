package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.model.Pengunjung
import com.project.projectuts.repository.PengunjungRepository
import kotlinx.coroutines.launch

class PengunjungViewModel(private val repository: PengunjungRepository) : ViewModel() {

    val allPengunjung: LiveData<List<Pengunjung>> = repository.getAllPengunjung()
    val statusMessage: MutableLiveData<String> = MutableLiveData()

    fun insertPengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            repository.insertPengunjung(pengunjung)
            statusMessage.value = "Pengunjung berhasil ditambahkan"
        }
    }

    fun deletePengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            repository.deletePengunjung(pengunjung)
            statusMessage.value = "Pengunjung berhasil dihapus"
        }
    }
}
