package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.project.books_library.api.ApiService
import com.project.books_library.dao.VisitorDao
import com.project.books_library.model.Visitors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VisitorRepository(
    private val apiService: ApiService,
    private val context: Context,
    private val visitorDao: VisitorDao
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun getVisitors(): LiveData<List<Visitors>> {
        val result = MediatorLiveData<List<Visitors>>()
        val localVisitors = visitorDao.getAllVisitors()
        result.addSource(localVisitors) { visitors ->
            result.value = visitors
        }

        if (isNetworkAvailable()) {
            coroutineScope.launch {
                val visitorsFromApi = apiService.getVisitors()
                val localVisitorsList = localVisitors.value ?: emptyList()
                val visitorsToSync = mutableListOf<Visitors>()
                val visitorsToDeleteOrSend = mutableListOf<Visitors>()

                localVisitorsList.forEach { localVisitor ->
                    visitorsFromApi.find { it.id_visitor == localVisitor.id_visitor }?.let { apiVisitor ->
                        if (localVisitor != apiVisitor) visitorsToSync.add(localVisitor)
                    } ?: visitorsToDeleteOrSend.add(localVisitor)
                }

                if (visitorsToSync.isNotEmpty()) showSyncChoiceDialog(visitorsToSync, visitorsFromApi)
                if (visitorsToDeleteOrSend.isNotEmpty()) showDeleteOrSendDialog(visitorsToDeleteOrSend)

                visitorsFromApi.forEach { apiVisitor ->
                    if (localVisitorsList.none { it.id_visitor == apiVisitor.id_visitor }) {
                        visitorDao.insertVisitor(apiVisitor)
                    }
                }
            }
        }
        return result
    }

    private fun showSyncChoiceDialog(visitorsToSync: List<Visitors>, visitorsFromApi: List<Visitors>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Konfirmasi Sinkronisasi")
                setMessage("Ada ${visitorsToSync.size} data yang telah berubah. Pilih sumber data mana yang ingin digunakan:")
                setPositiveButton("Gunakan Data Lokal") { dialog, _ ->
                    coroutineScope.launch {
                        visitorsToSync.forEach { localVisitor ->
                            apiService.editVisitor(localVisitor.id_visitor!!, localVisitor)
                            showToast("Data lokal untuk ${localVisitor.name} disinkronkan ke API.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Gunakan Data API") { dialog, _ ->
                    coroutineScope.launch {
                        visitorsFromApi.forEach { apiVisitor ->
                            visitorDao.updateVisitor(apiVisitor)
                            showToast("Data API untuk ${apiVisitor.name} disinkronkan ke database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
                create().show()
            }
        }
    }

    private fun showDeleteOrSendDialog(visitorsToDeleteOrSend: List<Visitors>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Data Tidak Ditemukan di API")
                setMessage("Ada ${visitorsToDeleteOrSend.size} data yang tidak ditemukan di API. Apakah Anda ingin menghapus semua data ini dari database lokal atau mengirimnya ke API?")
                setPositiveButton("Hapus Semua Data") { dialog, _ ->
                    coroutineScope.launch {
                        visitorsToDeleteOrSend.forEach { localVisitor ->
                            visitorDao.deleteById(localVisitor.id_visitor)
                            showToast("Data untuk ${localVisitor.name} dihapus dari database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Kirim Semua ke API") { dialog, _ ->
                    coroutineScope.launch {
                        visitorsToDeleteOrSend.forEach { localVisitor ->
                            apiService.insertVisitors(localVisitor)
                            showToast("Data untuk ${localVisitor.name} dikirim ke API.")
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

    suspend fun insertVisitor(visitor: Visitors) {
        if (isNetworkAvailable()) {
            apiService.insertVisitors(visitor)
        } else {
            visitorDao.insertVisitor(visitor)
        }
    }

    suspend fun editVisitor(visitor: Visitors): LiveData<List<Visitors>> {
        if (isNetworkAvailable()) {
            apiService.editVisitor(visitor.id_visitor!!, visitor)
        } else {
            visitorDao.updateVisitor(visitor)
        }
        return getVisitors()
    }

    suspend fun deleteVisitors(visitor: Visitors) {
        if (isNetworkAvailable()) {
            val response = apiService.deleteVisitor(visitor.id_visitor!!)
            if (response.isSuccessful) {
                visitorDao.deleteById(visitor.id_visitor)
            } else {
                throw Exception("Gagal Menghapus Pengunjung Dari API: ${response.message()}")
            }
        } else {
            visitorDao.deleteById(visitor.id_visitor)
        }
    }

    fun getAllVisitorName(): LiveData<List<String>> {
        return visitorDao.getAllVisitorNames()
    }
}