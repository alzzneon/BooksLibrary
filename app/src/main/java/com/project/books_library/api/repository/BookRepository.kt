package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.project.books_library.api.ApiService
import com.project.books_library.dao.BookDao
import com.project.books_library.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookRepository(
    private val apiService: ApiService,
    private val context: Context,
    private val bookDao: BookDao
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun getBooks(): LiveData<List<Book>> {
        val result = MediatorLiveData<List<Book>>()
        val localBooks = bookDao.getAllBooks()
        result.addSource(localBooks) { books ->
            result.value = books
        }

        if (isNetworkAvailable()) {
            coroutineScope.launch {
                val booksFromApi = apiService.getBooks()
                val localBookList = localBooks.value ?: emptyList()
                val bookToSync = mutableListOf<Book>()
                val bookToDeleteOrSend = mutableListOf<Book>()

                localBookList.forEach { localBooks ->
                    booksFromApi.find { it.id == localBooks.id }?.let { apiBook ->
                        if (localBooks != apiBook) bookToSync.add(localBooks)
                    } ?: bookToDeleteOrSend.add(localBooks)
                }

                if (bookToSync.isNotEmpty()) showSyncChoiceDialog(bookToSync, booksFromApi)
                if (bookToDeleteOrSend.isNotEmpty()) showDeleteOrSendDialog(bookToDeleteOrSend)

                booksFromApi.forEach { apiBook ->
                    if (localBookList.none { it.id == apiBook.id}) {
                        bookDao.insert(apiBook)
                    }
                }
            }
        }
        return result
    }
    private fun showSyncChoiceDialog(bookToSync: List<Book>, bookFromApi: List<Book>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Konfirmasi Sinkronisasi")
                setMessage("Ada ${bookToSync.size} data yang telah berubah. Pilih sumber data mana yang ingin digunakan:")
                setPositiveButton("Gunakan Data Lokal") { dialog, _ ->
                    coroutineScope.launch {
                        bookToSync.forEach { localbook ->
                            apiService.updateBook(localbook.id!!, localbook)
                            showToast("Data lokal untuk ${localbook.title} disinkronkan ke API.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Gunakan Data API") { dialog, _ ->
                    coroutineScope.launch {
                        bookFromApi.forEach { apiVisitor ->
                            bookDao.update(apiVisitor)
                            showToast("Data API untuk ${apiVisitor.title} disinkronkan ke database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
                create().show()
            }
        }
    }

    private fun showDeleteOrSendDialog(bookToDeleteOrSend: List<Book>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Data Tidak Ditemukan di API")
                setMessage("Ada ${bookToDeleteOrSend.size} data yang tidak ditemukan di API. Apakah Anda ingin menghapus semua data ini dari database lokal atau mengirimnya ke API?")
                setPositiveButton("Hapus Semua Data") { dialog, _ ->
                    coroutineScope.launch {
                        bookToDeleteOrSend.forEach { bookLocal ->
                            bookDao.deleteById(bookLocal.id)
                            showToast("Data untuk ${bookLocal.title} dihapus dari database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Kirim Semua ke API") { dialog, _ ->
                    coroutineScope.launch {
                        bookToDeleteOrSend.forEach { bookLocal ->
                            apiService.insertBook(bookLocal)
                            showToast("Data untuk ${bookLocal.title} dikirim ke API.")
                        }
                        dialog.dismiss()
                    }
                }
                setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
                create().show()
            }
        }
    }

    private fun showToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getBookById(id: Int): Book {
        return if (isNetworkAvailable()) {
            val book = apiService.getBookById(id)
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

    suspend fun editBook(book: Book): LiveData<List<Book>> {
        if (isNetworkAvailable()) {
            apiService.updateBook(book.id!!, book)
        } else {
            bookDao.update(book)
        }
        return getBooks()
    }

    suspend fun deleteBook(book: Book) {
        if (isNetworkAvailable()) {
            val response = apiService.deleteBook(book.id!!)
            if (response.isSuccessful) {
                bookDao.deleteById(book.id)
            } else {
                throw Exception("Gagal Menghapus Buku Dari API: ${response.message()}")
            }
        } else {
            bookDao.deleteById(book.id)
        }
    }
    fun getAllBookTitles(): LiveData<List<String>> {
        return bookDao.getAllBookTitles()
    }
}