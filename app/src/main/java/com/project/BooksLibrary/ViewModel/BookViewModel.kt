package com.project.BooksLibrary.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.BooksLibrary.API.Repository.BookRepository
import com.project.BooksLibrary.Model.Book
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    private val _booksLiveData = MutableLiveData<List<Book>>()
    val booksLiveData: LiveData<List<Book>> get() = _booksLiveData

    private val _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _errorliveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorliveData

//    private val _syncStatusLiveData = MutableLiveData<String>()
//    val syncStatusLiveData: LiveData<String> get() = _syncStatusLiveData

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