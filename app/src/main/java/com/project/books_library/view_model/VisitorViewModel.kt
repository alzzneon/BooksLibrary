package com.project.books_library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.books_library.api.repository.VisitorRepository
import com.project.books_library.model.Visitors
import kotlinx.coroutines.launch

class VisitorViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {
    val visitorsLiveData: LiveData<List<Visitors>> = visitorRepository.getVisitors()

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun insertVisitor(visitors: Visitors) {
        viewModelScope.launch {
            try {
                visitorRepository.insertVisitor(visitors)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi Kesalahan dalam menyimpan Pengunjung"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }

    fun deleteVisitor(visitors: Visitors) {
        viewModelScope.launch {
            try {
                visitorRepository.deleteVisitors(visitors)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam menghapus pengunjung"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }
}
