package com.project.books_library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.books_library.api.repository.BookRepository
import com.project.books_library.model.Book
import kotlinx.coroutines.launch

class BookViewModel(private val BooksRepository: BookRepository) : ViewModel() {

    val booksLiveData: LiveData<List<Book>> get() = BooksRepository.getBooks()

    private val _bookLiveData = MutableLiveData<Book>()
    val bookLiveData: LiveData<Book> get() = _bookLiveData

    private val _errorliveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorliveData

    fun fetchBookById(id: Int) {
        viewModelScope.launch {
            try {
                val book = BooksRepository.getBookById(id)
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
                    BooksRepository.insertBook(book)
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
                    BooksRepository.updateBook(updatedBook) //
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
                    BooksRepository.deleteBook(book)
                } catch (e: Exception) {
                    val errorMessage = e.message ?: "Terjadi kesalahan dalam menghapus data buku"
                    _errorliveData.postValue(errorMessage)
                }
            }
        }
    }
