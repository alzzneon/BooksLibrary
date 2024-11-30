package com.project.projectuts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.projectuts.API.BookRepository
import com.project.projectuts.model.Book
import kotlinx.coroutines.launch

class BukuViewModel(private val repository: BookRepository) : ViewModel() {

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _errorliveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorliveData

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
    fun fetchBookById(id: Int) {
        viewModelScope.launch {
            try {
                val book = repository.getBookById(id)
                _bookLiveData.postValue(book)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Terjadi kesalahan dalam mengambil detail buku"
                _errorliveData.postValue(errorMessage)
            }
        }
    }

    fun updateBuku(updatedBook: Book) {

    }

    fun deleteBuku(book: Book) {

    }
}
