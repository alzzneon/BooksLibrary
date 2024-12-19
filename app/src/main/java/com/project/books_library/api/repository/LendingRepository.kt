package com.project.books_library.api.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.project.books_library.api.ApiService
import com.project.books_library.dao.LendingDao
import com.project.books_library.model.Lending
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LendingRepository(
    private val apiService: ApiService,
    private val context: Context,
    private val lendingDao: LendingDao
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun getLendings(): LiveData<List<Lending>> {
        val result = MediatorLiveData<List<Lending>>()
        val localLendings = lendingDao.getAllLendings()
        result.addSource(localLendings) { lendings -> result.value = lendings }

        if (isNetworkAvailable()) {
            coroutineScope.launch {
                val lendingsFromApi = apiService.getLendings()
                val localLendingsList = localLendings.value ?: emptyList()
                val lendingsToSync = mutableListOf<Lending>()
                val lendingsToDeleteOrSend = mutableListOf<Lending>()

                localLendingsList.forEach { localLending ->
                    lendingsFromApi.find { it.id_lending == localLending.id_lending }?.let { apiLending ->
                        if (localLending != apiLending) lendingsToSync.add(localLending)
                    } ?: lendingsToDeleteOrSend.add(localLending)
                }

                if (lendingsToSync.isNotEmpty()) showSyncChoiceDialog(lendingsToSync, lendingsFromApi)
                if (lendingsToDeleteOrSend.isNotEmpty()) showDeleteOrSendDialog(lendingsToDeleteOrSend)

                lendingsFromApi.forEach { apiLending ->
                    if (localLendingsList.none { it.id_lending == apiLending.id_lending }) {
                        lendingDao.insertLending(apiLending)
                    }
                }
            }
        }
        return result
    }

    private fun showSyncChoiceDialog(lendingsToSync: List<Lending>, lendingsFromApi: List<Lending>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Konfirmasi Sinkronisasi")
                setMessage("Ada ${lendingsToSync.size} data yang telah berubah. Pilih sumber data mana yang ingin digunakan:")
                setPositiveButton("Gunakan Data Lokal") { dialog, _ ->
                    coroutineScope.launch {
                        lendingsToSync.forEach { localLending ->
                            apiService.editLending(localLending.id_lending!!, localLending)
                            showToast("Data lokal untuk peminjaman ID ${localLending.id_lending} disinkronkan ke API.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Gunakan Data API") { dialog, _ ->
                    coroutineScope.launch {
                        lendingsFromApi.forEach { apiLending ->
                            lendingDao.updateLending(apiLending)
                            showToast("Data API untuk peminjaman ID ${apiLending.id_lending} disinkronkan ke database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
                create().show()
            }
        }
    }

    private fun showDeleteOrSendDialog(lendingsToDeleteOrSend: List<Lending>) {
        CoroutineScope(Dispatchers.Main).launch {
            AlertDialog.Builder(context).apply {
                setTitle("Data Tidak Ditemukan di API")
                setMessage("Ada ${lendingsToDeleteOrSend.size} data yang tidak ditemukan di API. Apakah Anda ingin menghapus semua data ini dari database lokal atau mengirimnya ke API?")
                setPositiveButton("Hapus Semua Data") { dialog, _ ->
                    coroutineScope.launch {
                        lendingsToDeleteOrSend.forEach { localLending ->
                            lendingDao.deleteLendingById(localLending.id_lending!!)
                            showToast("Data untuk peminjaman ID ${localLending.id_lending} dihapus dari database lokal.")
                        }
                        dialog.dismiss()
                    }
                }
                setNegativeButton("Kirim Semua ke API") { dialog, _ ->
                    coroutineScope.launch {
                        lendingsToDeleteOrSend.forEach { localLending ->
                            apiService.insertLending(localLending)
                            showToast("Data untuk peminjaman atas Nama ${localLending.visitor_name} dikirim ke API.")
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

    suspend fun insertLending(lending: Lending) {
        if (isNetworkAvailable()) {
            apiService.insertLending(lending)
        } else {
            lendingDao.insertLending(lending)
        }
    }

    suspend fun editLending(lending: Lending): LiveData<List<Lending>> {
        if (isNetworkAvailable()) {
            apiService.editLending(lending.id_lending!!, lending)
        } else {
            lendingDao.updateLending(lending)
        }
        return getLendings()
    }

    suspend fun deleteLending(lending: Lending) {
        if (isNetworkAvailable()) {
            val response = apiService.deleteLending(lending.id_lending!!)
            if (response.isSuccessful) {
                lendingDao.deleteLendingById(lending.id_lending)
            } else {
                throw Exception("Gagal Menghapus Peminjaman Dari API: ${response.message()}")
            }
        } else {
            lendingDao.deleteLendingById(lending.id_lending!!)
        }
    }
}
