package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.project.books_library.api.ApiService
import com.project.books_library.dao.BookDao
import com.project.books_library.model.Book

class BookRepository(private val apiService: ApiService, private val context: Context, private val bookDao: BookDao) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    suspend fun getBooks(): List<Book> {
        return if (isNetworkAvailable()) {
            val booksFromApi = apiService.getBooks()
            val existingBooks = bookDao.getAllBooks().map { it.title }.toSet()
            val newBooks = booksFromApi.filter { it.title !in existingBooks }
            if (newBooks.isNotEmpty()) {
                bookDao.insertListBooks(newBooks)
            }
            val localBooks = bookDao.getAllBooks()
            val apiBookTitles = booksFromApi.map { it.title }.toSet()
            localBooks.forEach { localBook ->
                if (localBook.title !in apiBookTitles) {
                    try {
                        apiService.insertBook(localBook)
                        Log.d("BookRepository", "Buku Dikirimkan ke API: ${localBook.title}")
                    } catch (e: Exception) {
                        Log.e("BookRepository", "Gagal Mengirimkan Buku ke API: ${e.message}")
                    }
                }
            }
            bookDao.getAllBooks()
        } else {
            bookDao.getAllBooks()
        }
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
//        if (isNetworkAvailable()) {
//            apiService.(updatedBook)
//        } else {
//            bookDao.update(updatedBook)
        }

    suspend fun deleteBook(book: Book) {
//        if (isNetworkAvailable()) {
//            book.id?.let { apiService.deleteBook(it) }
//        } else {
//            bookDao.deleteById(book.id)
//        }
    }
}