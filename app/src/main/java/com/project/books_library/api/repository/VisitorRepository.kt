package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.project.books_library.api.ApiService
import com.project.books_library.dao.VisitorDao
import com.project.books_library.model.Visitors

class VisitorRepository(private val apiService: ApiService, private val context: Context, private val visitorDao: VisitorDao) {
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    suspend fun getVisitors(): List<Visitors> {
        return if (isNetworkAvailable()) {
            val visitorsFromApi = apiService.getVisitors()
            val existingVisitors = visitorDao.getAllVisitors().map { it.name }.toSet()
            val newVisitors = visitorsFromApi.filter { it.name !in existingVisitors }
            if (newVisitors.isNotEmpty()) {
                visitorDao.insertListVisitors(newVisitors)
            }

            val localVisitors = visitorDao.getAllVisitors()
            val apiVisitorNames = visitorsFromApi.map { it.name }.toSet()
            localVisitors.forEach { localVisitor ->
                if (localVisitor.name !in apiVisitorNames) {
                    try {
                        apiService.insertVisitors(localVisitor)
                        Log.d("VisitorRepository", "Visitor dikirimkan ke API: ${localVisitor.name}")
                    } catch (e: Exception) {
                        Log.e("VisitorRepository", "Gagal Mengirimkan Visitors ke API: ${e.message}")
                    }
                }
            }
            visitorDao.getAllVisitors()
        } else {
            visitorDao.getAllVisitors()
        }
    }


    suspend fun insertVisitor(visitors: Visitors) {
        return if (isNetworkAvailable()) {
            apiService.insertVisitors(visitors)
        } else {
            visitorDao.insertVisitor(visitors)
        }
    }
}