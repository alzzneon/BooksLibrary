package com.project.projectuts.API

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.project.projectuts.dao.BookDao
import com.project.projectuts.model.Book

class BookRepository(private val apiService: ApiService, private val context: Context, private val bookDao: BookDao) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    suspend fun getBooks(): List<Book> {
        return if (isNetworkAvailable()) {
            val books = apiService.getBooks()
            bookDao.insertBooks(books)
            books
        } else {
            bookDao.getAllBooks()
        }
    }

    suspend fun insertBook(book: Book) {
        if (isNetworkAvailable()) {
            apiService.insertBook(book)
        } else {
            bookDao.insert(book)
        }
    }

    suspend fun syncUnsentBooks() {
        if (!isNetworkAvailable()) {
            throw Exception("Tidak ada koneksi internet")
        }
        val unsentBooks = bookDao.getAllBooks()
        if (unsentBooks.isEmpty()) {
            return
        }
        val existingBookTitles = apiService.getBooks().map { it.title }.toSet()
        unsentBooks.forEach { book ->
            if (book.title !in existingBookTitles) {
                try {
                    apiService.insertBook(book)
                    bookDao.deleteById(book.id)
                } catch (e: Exception) {
                    throw Exception("Gagal mengirim buku: ${book.title}", e)
                }
            }
        }

        Log.d("SyncData", "Sinkronisasi selesai.")
    }

    suspend fun updateBook(updatedBook: Book) {
//        if (isNetworkAvailable()) {
//            apiService.(updatedBook)
//        } else {
//            bookDao.update(updatedBook)
        }

    suspend fun deleteBook(book: Book) {
//        if (isNetworkAvailable()) {
//            apiService.deleteBook(book.id)
//        } else {
//            bookDao.delete(book)
//        }
    }
}
