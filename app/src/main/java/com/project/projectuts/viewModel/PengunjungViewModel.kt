package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.dao.PengunjungDao
import com.project.projectuts.model.Pengunjung
import kotlinx.coroutines.launch

class PengunjungViewModel(private val pengunjungDao: PengunjungDao) : ViewModel() {

    val allPengunjung: LiveData<List<Pengunjung>> = pengunjungDao.getAllPengunjung()
    val statusMessage: MutableLiveData<String> = MutableLiveData()

    fun insertPengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            pengunjungDao.insertPengunjung(pengunjung)
            statusMessage.value = "Pengunjung berhasil ditambahkan"
        }
    }

    fun deletePengunjung(pengunjung: Pengunjung) {
        viewModelScope.launch {
            pengunjungDao.deletePengunjung(pengunjung)
            statusMessage.value = "Pengunjung berhasil dihapus"
        }
    }
}
