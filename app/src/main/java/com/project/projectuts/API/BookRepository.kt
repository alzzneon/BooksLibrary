package com.project.projectuts.API

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.project.projectuts.dao.BookDao
import com.project.projectuts.model.Book

class BookRepository(private val apiService: ApiService, private val context: Context,private val bookDao: BookDao) {

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
        if (isNetworkAvailable()) {
            val unsentBooks = bookDao.getAllBooks()
            if (unsentBooks.isEmpty()) {
                return
            }
            val existingBooks = apiService.getBooks()
            val existingBookTitles = existingBooks.map { it.title }.toSet()
            for (book in unsentBooks) {
                if (!existingBookTitles.contains(book.title)) {
                    try {
                        apiService.insertBook(book)
                        bookDao.deleteById(book.id)
                    } catch (e: Exception) {
                        throw Exception("Gagal mengirim buku: ${book.title}")
                    }
                }
            }
            Log.d("SyncData", "Sinkronisasi selesai.")
        } else {
            throw Exception("Tidak ada koneksi internet")
        }
    }

    // belum di fix
    suspend fun updateBook(updatedBook: Book) {

    }

    // belum di fix
    suspend fun deleteBook(book: Book) {

    }
}