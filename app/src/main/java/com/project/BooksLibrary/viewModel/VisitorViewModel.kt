package com.project.BooksLibrary.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.BooksLibrary.API.repository.VisitorRepository
import com.project.BooksLibrary.model.Visitors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VisitorViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {
    private val _visitorsLiveData = MutableLiveData<List<Visitors>>()
    val visitorsLiveData: LiveData<List<Visitors>> get() = _visitorsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun fetchVisitors() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    visitorRepository.getVisitors()
                }
                _visitorsLiveData.postValue(result)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam mengambil data pengunjung"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }
}
