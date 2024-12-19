package com.project.books_library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.books_library.api.repository.LendingRepository
import com.project.books_library.model.Lending
import kotlinx.coroutines.launch

class LendingViewModel(private val lendingRepository: LendingRepository) : ViewModel() {
    val lendingLiveData: LiveData<List<Lending>> = lendingRepository.getLendings()

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun insertLending(lending: Lending) {
        viewModelScope.launch {
            try {
                lendingRepository.insertLending(lending)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam menyimpan Peminjam"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }

    fun deleteLending(lending: Lending) {
        viewModelScope.launch {
            try {
                lendingRepository.deleteLending(lending)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam menghapus Peminjam"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }

    fun editLending(lending: Lending) {
        viewModelScope.launch {
            try {
                lendingRepository.editLending(lending)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi Kesalahan dalam mengedit Peminjam"
                _errorLiveData.postValue(errorMessage)
            }
        }
    }

}
