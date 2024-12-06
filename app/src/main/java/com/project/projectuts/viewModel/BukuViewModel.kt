package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.API.BookRepository
import com.project.projectuts.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BukuViewModel(private val repository: BookRepository) : ViewModel() {

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _errorliveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorliveData

    private val _syncStatusLiveData = MutableLiveData<String>()
    val syncStatusLiveData: LiveData<String> get() = _syncStatusLiveData

    fun fetchBooks() {
        viewModelScope.launch {
            try {
                val books = repository.getBooks()
                _booksLiveData.postValue(books)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam mengambil data buku"
                _errorliveData.postValue(errorMessage)
            }
        }
    }

    fun insertBuku(book: Book) {
        viewModelScope.launch {
            try {
                repository.insertBook(book)
                fetchBooks()
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam menyimpan data buku"
                _errorliveData.postValue(errorMessage)
            }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            _syncStatusLiveData.postValue("Proses sinkronisasi dimulai...")
            try {
                repository.syncUnsentBooks()
                _syncStatusLiveData.postValue("Semua buku berhasil disinkronkan!")
            } catch (e: Exception) {
                _syncStatusLiveData.postValue("Sinkronisasi gagal: ${e.message}")
            }
        }
    }

    fun refreshBooks() {
        viewModelScope.launch {
            syncData()
            fetchBooks()
        }
    }

    // belum di fix
    fun updateBook(updatedBook: Book) {
        viewModelScope.launch {
            try {
                repository.updateBook(updatedBook) //
                fetchBooks()
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam memperbarui data buku"
                _errorliveData.postValue(errorMessage)
            }
        }
    }

    // belum di fix
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            try {
                repository.deleteBook(book)
                fetchBooks()
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam menghapus data buku"
                _errorliveData.postValue(errorMessage)
            }
        }
    }
}
