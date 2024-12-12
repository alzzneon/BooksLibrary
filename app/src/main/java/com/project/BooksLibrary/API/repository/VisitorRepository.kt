package com.project.BooksLibrary.API.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.project.BooksLibrary.API.ApiService
import com.project.BooksLibrary.dao.VisitorDao
import com.project.BooksLibrary.model.Visitors

class VisitorRepository(private val apiService: ApiService, private val context: Context, private val visitorDao: VisitorDao) {
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    suspend fun getVisitors(): List<Visitors> {
        return if (isNetworkAvailable()) {
            val visitor = apiService.getVisitors()
            visitorDao.insertVisitors(visitor)
            visitor
        } else {
            visitorDao.getAllVisitors()
        }
    }
}