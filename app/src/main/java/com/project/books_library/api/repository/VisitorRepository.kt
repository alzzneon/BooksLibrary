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

class VisitorRepository(private val apiService: ApiService, private val context: Context, private val visitorDao: VisitorDao) {

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
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val visitorsFromApi: List<Visitors> = apiService.getVisitors()
                    val existingVisitors = localVisitors.value?.map { it.name }?.toSet() ?: emptySet()
                    val newVisitors = visitorsFromApi.filter { it.name !in existingVisitors }

                    if (newVisitors.isNotEmpty()) {
                        visitorDao.insertListVisitors(newVisitors)
                    }
                    localVisitors.value?.forEach { localVisitor ->
                        if (localVisitor.name !in visitorsFromApi.map { it.name }) {
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

    suspend fun insertVisitor(visitor: Visitors) {
        if (isNetworkAvailable()) {
            apiService.insertVisitors(visitor)
        } else {
            visitorDao.insertVisitor(visitor)
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