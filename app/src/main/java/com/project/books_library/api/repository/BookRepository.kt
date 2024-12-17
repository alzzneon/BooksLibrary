package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.project.books_library.api.ApiService
import com.project.books_library.dao.BookDao
import com.project.books_library.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookRepository(private val apiService: ApiService, private val context: Context, private val bookDao: BookDao) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun getBooks(): LiveData<List<Book>> {
        val result = MediatorLiveData<List<Book>>()
        val localBooks = bookDao.getAllBooks()
        result.addSource(localBooks) { books ->
            result.value = books
        }

        if (isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val booksFromApi: List<Book> = apiService.getBooks()
                    val existingBooks = localBooks.value?.map { it.title }?.toSet() ?: emptySet()
                    val newBooks = booksFromApi.filter { it.title !in existingBooks }

                    if (newBooks.isNotEmpty()) {
                        bookDao.insertListBooks(newBooks)
                    }

                    localBooks.value?.forEach { localBook ->
                        if (localBook.title !in booksFromApi.map { it.title }) {
                            try {
                                apiService.insertBook(localBook)
                                Log.d(
                                    "BookRepository",
                                    "Buku dikirimkan ke API: ${localBook.title}"
                                )
                            } catch (e: Exception) {
                                Log.e(
                                    "BookRepository",
                                    "Gagal Mengirimkan Buku ke API: ${e.message}"
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("BookRepository", "Gagal mengambil data dari API: ${e.message}")
                }
            }
        }

        return result
    }

    suspend fun getBookById(id: Int): Book {
        return if (isNetworkAvailable()) {
            val book = apiService.getBookById(id)
            bookDao.insertListBooks(listOf(book))
            book
        } else {
            bookDao.getBookById(id)
        }
    }

    suspend fun insertBook(book: Book) {
        if (isNetworkAvailable()) {
            apiService.insertBook(book)
        } else {
            bookDao.insert(book)
        }
    }

    suspend fun updateBook(updatedBook: Book) {
        if (isNetworkAvailable()) {
            try {
                // Ensure the book has an ID for updating
                updatedBook.id?.let { id ->
                    // Call API to update the book
                    val updatedBookResponse = apiService.updateBook(id, updatedBook)

                    // Update local database
                    bookDao.insert(updatedBookResponse)

                    Log.d("BookRepository", "Buku berhasil diperbarui: ${updatedBook.title}")
                } ?: throw IllegalArgumentException("Book ID cannot be null")
            } catch (e: Exception) {
                Log.e("BookRepository", "Gagal memperbarui buku: ${e.message}")
                // If API update fails, update local database
                bookDao.insert(updatedBook)
            }
        } else {
            // If no network, update local database
            bookDao.insert(updatedBook)
        }
    }

    suspend fun deleteBook(book: Book) {
        if (isNetworkAvailable()) {
            try {
                // Menghapus buku dari API menggunakan ID buku
                book.id?.let {
                    apiService.deleteBook(it)
                    Log.d("BookRepository", "Buku berhasil dihapus dari API: ${book.title}")
                }
            } catch (e: Exception) {
                Log.e("BookRepository", "Gagal menghapus buku dari API: ${e.message}")
            }
        }
        // Menghapus buku secara lokal di database
        book.id?.let {
            bookDao.deleteById(it)
            Log.d("BookRepository", "Buku berhasil dihapus dari database lokal: ${book.title}")
        }
    }


}