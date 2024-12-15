package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.project.books_library.api.ApiService
import com.project.books_library.dao.VisitorDao
import com.project.books_library.model.Visitors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog

class VisitorRepository(
    private val apiService: ApiService,
    private val context: Context,
    private val visitorDao: VisitorDao
) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    suspend fun insertVisitor(visitor: Visitors) {
        if (isNetworkAvailable()) {
            apiService.insertVisitors(visitor)
        } else {
            visitorDao.insertVisitor(visitor)
        }
    }

    fun getVisitors(): LiveData<List<Visitors>> {
        val result = MediatorLiveData<List<Visitors>>()
        val localVisitors = visitorDao.getAllVisitors()
        result.addSource(localVisitors) { visitors ->
            result.value = visitors
        }

        if (isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val visitorsFromApi: List<Visitors> = apiService.getVisitors()
                    val existingVisitors = localVisitors.value?.map { it.id_visitor }?.toSet() ?: emptySet()
                    val newVisitors = visitorsFromApi.filter { it.id_visitor !in existingVisitors }
                    if (newVisitors.isNotEmpty()) {
                        visitorDao.insertListVisitors(newVisitors)
                    }
                    val apiVisitorSet = visitorsFromApi.associateBy { it.id_visitor }
                    localVisitors.value?.forEach { localVisitor ->
                        val apiVisitor = apiVisitorSet[localVisitor.id_visitor]
                        if (apiVisitor != null) {
                            if (localVisitor != apiVisitor) {
                                showDataConflictDialog(localVisitor, apiVisitor)
                            }
                        } else {
                            try {
                                apiService.insertVisitors(localVisitor)
                                Log.d("VisitorRepository", "Visitor dikirimkan ke API: ${localVisitor.name}")
                            } catch (e: Exception) {
                                Log.e("VisitorRepository", "Gagal Mengirimkan Visitors ke API: ${e.message}")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("VisitorRepository", "Gagal mengambil data dari API: ${e.message}")
                }
            }
        }
        return result
    }

    private fun showDataConflictDialog(localVisitor: Visitors, apiVisitor: Visitors) {
        CoroutineScope(Dispatchers.Main).launch {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Data Conflict")
            builder.setMessage("Data untuk ${localVisitor.name} telah berubah. Pilih data mana yang ingin digunakan: ")

            builder.setPositiveButton("Gunakan Data Lokal") { dialog, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    visitorDao.updateVisitor(localVisitor)
                    Log.d("VisitorRepository", "Data lokal digunakan untuk ${localVisitor.name}.")

                    if (isNetworkAvailable()) {
                        try {
                            apiService.editVisitor(localVisitor.id_visitor!!, localVisitor)
                            Log.d("VisitorRepository", "Data lokal dikirim ke API untuk ${localVisitor.name}.")
                        } catch (e: Exception) {
                            Log.e("VisitorRepository", "Gagal mengirim data lokal ke API: ${e.message}")
                        }
                    }
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("Gunakan Data API") { dialog, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    visitorDao.updateVisitor(apiVisitor)
                    Log.d("VisitorRepository", "Data API digunakan untuk ${apiVisitor.name}.")
                }
                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    suspend fun editVisitor(visitor: Visitors) {
        visitorDao.updateVisitor(visitor)
        if (isNetworkAvailable()) {
            try {
                apiService.editVisitor(visitor.id_visitor!!, visitor)
                Log.d("VisitorRepository", "Visitor berhasil diperbarui di API: ${visitor.name}")
            } catch (e: Exception) {
                Log.e("VisitorRepository", "Gagal memperbarui visitor di API: ${e.message}")
            }
        } else {
            Log.d("VisitorRepository", "Visitor diperbarui di database lokal: ${visitor.name}")
        }
    }

    suspend fun deleteVisitors(visitor: Visitors) {
        if (isNetworkAvailable()) {
            try {
                visitor.id_visitor?.let {
                    apiService.deleteVisitor(it)
                    visitorDao.deleteById(it)
                    Log.d("VisitorRepository", "Visitor berhasil dihapus dari API dan database lokal: ${visitor.name}")
                }
            } catch (e: Exception) {
                Log.e("VisitorRepository", "Gagal menghapus visitor dari API: ${e.message}")
            }
        } else {
            visitorDao.deleteById(visitor.id_visitor)
            Log.d("VisitorRepository", "Visitor dihapus dari database lokal: ${visitor.name}")
        }
    }
}
