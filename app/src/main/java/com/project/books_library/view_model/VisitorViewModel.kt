package com.project.books_library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.books_library.api.repository.VisitorRepository
import com.project.books_library.model.Visitors
import kotlinx.coroutines.launch

class VisitorViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {
    private val _visitorsLiveData = MutableLiveData<List<Visitors>>()
    val visitorsLiveData: LiveData<List<Visitors>> get() = _visitorsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

//    private val _syncStatusLiveData = MutableLiveData<String>()
//    val syncStatusLiveData: LiveData<String> get() = _syncStatusLiveData

    fun fetchVisitors() {
        viewModelScope.launch {
            try {
                val visitors = visitorRepository.getVisitors()
                _visitorsLiveData.postValue(visitors)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam mengambil data pengunjung"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }

    fun insertVisitor(visitors: Visitors) {
        viewModelScope.launch {
            try {
                visitorRepository.insertVisitor(visitors)
                fetchVisitors()
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi Kesalahan dalam menyimpan Pengunjung"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }
//    }
//    fun syncVisitors() {
//        viewModelScope.launch {
//            _syncStatusLiveData.postValue("Proses sinkronisasi dimulai...")
//            try {
//                visitorRepository.syncUnsentVisitor()
//                _syncStatusLiveData.postValue("Semua Visitors berhasil di sinkronkan!")
//            } catch (e: Exception) {
//                _syncStatusLiveData.postValue("Sinkronasi gagal: ${e.message}")
//            }
//        }
//    }
}
